package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import com.dnd.spaced.core.auth.application.exception.BlockedTokenException;
import com.dnd.spaced.core.auth.application.exception.ExpiredTokenException;
import com.dnd.spaced.core.auth.application.exception.RotationRefreshTokenMismatchException;
import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.dnd.spaced.core.auth.domain.repository.BlacklistTokenRepository;
import com.dnd.spaced.core.auth.domain.repository.RefreshTokenRotationRepository;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwtEncoder;
import com.dnd.spaced.core.auth.infrastructure.jwt.exception.InvalidTokenException;
import com.dnd.spaced.fixture.LocalDateTimeFixture;
import com.dnd.spaced.global.config.properties.TokenProperties;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    TokenEncoder tokenEncoder;

    @Autowired
    BlacklistTokenService blacklistTokenService;

    @Autowired
    BlacklistTokenRepository blacklistTokenRepository;

    @Autowired
    RefreshTokenRotationRepository refreshTokenRotationRepository;

    @Test
    void 기존_refreshToken을_통해_토큰을_갱신한다() {
        // given
        Long accountId = 1L;
        String refreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                accountId,
                "ROLE_USER"
        );

        // when
        TokenDto token = tokenService.refreshToken(refreshToken);

        // then
        Optional<String> rtt = refreshTokenRotationRepository.findBy(accountId);

        assertAll(
                () -> assertThat(token.accessToken()).isNotBlank(),
                () -> assertThat(token.refreshToken()).isNotBlank(),
                () -> assertThat(rtt).isPresent()
        );
    }

    @Test
    void Bearer_타입의_토큰이_아니라면_토큰_갱신을_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> tokenService.refreshToken("Basic refresh token"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("유효한 토큰이 아닙니다.");
    }

    @Test
    void 만료된_refreshToken을_전달하면_토큰_갱신을_할_수_없다() {
        // given
        String refreshToken = tokenEncoder.encode(
                LocalDateTimeFixture.from("2000-02-02 13:13:00"),
                TokenType.REFRESH,
                1L,
                "ROLE_USER"
        );

        // when & then
        assertThatThrownBy(() -> tokenService.refreshToken(refreshToken))
                .isInstanceOf(ExpiredTokenException.class)
                .hasMessage("Refresh Token이 만료되었습니다.");
    }

    @Test
    void 길이가_유효하지_않은_refreshToken을_전달하면_토큰_갱신을_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> tokenService.refreshToken("Bearer abcde"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("유효한 토큰이 아닙니다.");
    }

    @ParameterizedTest(name = "refreshToken이 {0}일 때 토큰 갱신을 할 수 없다")
    @NullAndEmptySource
    void 비어_있는_refreshToken을_전달하면_토큰_갱신을_할_수_없다(String invalidRefreshToken) {
        // when & then
        assertThatThrownBy(() -> tokenService.refreshToken(invalidRefreshToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("토큰이 존재하지 않거나 길이가 부족합니다.");
    }

    @Test
    void 다른_서비스에서_생성한_토큰을_전달하면_토큰_갱신을_할_수_없다() {
        TokenProperties tokenProperties = new TokenProperties(
                "thisistoolargeaccesstokenkeyfordummykeydatafortest",
                "thisistoolargerefreshtokenkeyfordummykeydatafortest",
                "other-issuer",
                43200,
                259200,
                43200000L,
                259200000L
        );
        JwtEncoder jwtEncoder = new JwtEncoder(tokenProperties);
        String refreshToken = jwtEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                1L,
                "ROLE_USER"
        );

        // when & then
        assertThatThrownBy(() -> tokenService.refreshToken(refreshToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("서비스에서 발급한 토큰이 아닙니다.");
    }

    @Test
    void 블랙리스트로_등록된_회원의_refreshToken을_전달하면_토큰_갱신을_할_수_없다() {
        // given
        Long accountId = 1L;
        String refreshToken = tokenEncoder.encode(
                LocalDateTime.now().minusMinutes(3L),
                TokenType.REFRESH,
                accountId,
                "ROLE_USER"
        );

        blacklistTokenService.register(accountId);

        // when & then
        assertThatThrownBy(() -> tokenService.refreshToken(refreshToken))
                .isInstanceOf(BlockedTokenException.class)
                .hasMessage("블랙리스트로 등록된 토큰입니다.");
    }

    @Test
    void 전달한_refreshToken_값이_RTT로_저장한_값과_일치하지_않으면_토큰_갱신을_할_수_없다() {
        // given
        Long accountId = 1L;
        String refreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                accountId,
                "ROLE_USER"
        );

        refreshTokenRotationRepository.save(accountId, "refresh token");

        // when & then
        assertThatThrownBy(() -> tokenService.refreshToken(refreshToken))
                .isInstanceOf(RotationRefreshTokenMismatchException.class)
                .hasMessage("기존 Refresh Token과 일치하지 않습니다.");

        assertThat(blacklistTokenRepository.findBy(accountId)).isPresent();
    }
}
