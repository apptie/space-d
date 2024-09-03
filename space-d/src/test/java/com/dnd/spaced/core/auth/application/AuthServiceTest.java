package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpRedis;
import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import com.dnd.spaced.core.auth.application.exception.BlockedTokenException;
import com.dnd.spaced.core.auth.application.exception.ExpiredTokenException;
import com.dnd.spaced.core.auth.application.exception.RotationRefreshTokenMismatchException;
import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.domain.TokenType;
import com.dnd.spaced.core.auth.domain.repository.BlacklistTokenRepository;
import com.dnd.spaced.core.auth.domain.repository.RefreshTokenRotationRepository;
import com.dnd.spaced.core.auth.infrastructure.JwtEncoder;
import com.dnd.spaced.core.auth.infrastructure.exception.InvalidTokenException;
import com.dnd.spaced.global.config.properties.TokenProperties;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@CleanUpRedis
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    TokenEncoder tokenEncoder;

    @Autowired
    BlacklistTokenService blacklistTokenService;

    @Autowired
    BlacklistTokenRepository blacklistTokenRepository;

    @Autowired
    RefreshTokenRotationRepository refreshTokenRotationRepository;

    @Test
    void refreshToken_메서드는_Bearer_타입의_토큰이_아닌_경우_InvalidTokenException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> authService.refreshToken("Basic refresh token"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Bearer 타입의 토큰이 아닙니다.");
    }

    @Test
    void refreshToken_메서드는_만료된_refreshToken을_전달하는_경우_ExpiredTokenException_예외가_발생한다() {
        // given
        String refreshToken = tokenEncoder.encode(
                LocalDateTime.now().minusYears(3L),
                TokenType.REFRESH,
                "id",
                "roleName"
        );

        // when & then
        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(ExpiredTokenException.class)
                .hasMessage("Refresh Token이 만료되었습니다.");
    }

    @Test
    void refreshToken_메서드는_유효하지_않은_refreshToken을_전달하면_InvalidTokenException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> authService.refreshToken("Bearer abcde"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("유효한 토큰이 아닙니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void refreshToken_메서드는_null이나_길이가_부족한_refreshToken을_전달하면_InvalidTokenException_예외가_발생한다(
            String invalidRefreshToken) {
        // when & then
        assertThatThrownBy(() -> authService.refreshToken(invalidRefreshToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("토큰이 존재하지 않거나 길이가 부족합니다.");
    }

    @Test
    void refreshToken_메서드는_다른_서비스에서_발급한_refreshToken을_전달하면_InvalidTokenException_예외가_발생한다() {
        String accountId = "id";
        TokenProperties tokenProperties = new TokenProperties(
                "thisistoolargeaccesstokenkeyfordummykeydatafortest",
                "thisistoolargerefreshtokenkeyfordummykeydatafortest",
                "otherissuer",
                43200,
                259200,
                43200000L,
                259200000L
        );
        JwtEncoder jwtEncoder = new JwtEncoder(tokenProperties);
        String refreshToken = jwtEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                accountId,
                "roleName"
        );

        // when & then
        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("서비스에서 발급한 토큰이 아닙니다.");
    }

    @Test
    void refreshToken_메서드는_블랙리스트로_등록된_경우_BlockedTokenException_예외가_발생한다() {
        // given
        String accountId = "id";
        LocalDateTime now = LocalDateTime.now();
        String refreshToken = tokenEncoder.encode(
                now.minusMinutes(10L),
                TokenType.REFRESH,
                accountId,
                "roleName"
        );

        blacklistTokenService.register(accountId);

        // when & then
        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(BlockedTokenException.class)
                .hasMessage("블랙리스트로 등록된 토큰입니다.");
    }

    @Test
    void refreshToken_메서드는_refresh_token_rotation으로_등록된_값과_일치하지_않으면_RotationRefreshTokenMismatchException_예외가_발생한다() {
        // given
        String accountId = "id";
        String refreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                accountId,
                "roleName"
        );

        refreshTokenRotationRepository.save(accountId, "refresh token");

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                        .isInstanceOf(RotationRefreshTokenMismatchException.class)
                        .hasMessage("기존 Refresh Token과 일치하지 않습니다."),
                () -> assertThat(blacklistTokenRepository.findBy(accountId)).isPresent()
        );
    }

    @Test
    void refreshToken_메서드는_유효한_refreshToken을_전달하면_새로운_accessToken과_refreshToken을_초기화해_반환한다() {
        // given
        String accountId = "id";
        String refreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                accountId,
                "roleName"
        );

        // when
        TokenDto actual = authService.refreshToken(refreshToken);

        // then
        assertAll(
                () -> assertThat(actual.accessToken()).startsWith("Bearer "),
                () -> assertThat(actual.refreshToken()).startsWith("Bearer "),
                () -> assertThat(refreshTokenRotationRepository.findBy(accountId)).isPresent()
        );
    }
}
