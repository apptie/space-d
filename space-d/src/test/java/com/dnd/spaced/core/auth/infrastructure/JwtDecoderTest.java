package com.dnd.spaced.core.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.auth.domain.PrivateClaims;
import com.dnd.spaced.core.auth.domain.TokenType;
import com.dnd.spaced.core.auth.infrastructure.exception.InvalidTokenException;
import com.dnd.spaced.global.config.properties.TokenProperties;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtDecoderTest {

    TokenProperties tokenProperties = new TokenProperties(
            "thisistoolargeaccesstokenkeyfordummykeydatafortest",
            "thisistoolargerefreshtokenkeyfordummykeydatafortest",
            "issuer",
            43200,
            259200,
            43200000L,
            259200000L
    );
    JwtDecoder jwtDecoder = new JwtDecoder(tokenProperties);

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void 유효하지_않은_토큰을_인코딩_할_수_없다(TokenType tokenType) {
        // given
        String invalidToken = "Bearer invalid";

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(tokenType, invalidToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("유효한 토큰이 아닙니다.");
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void 만료된_토큰을_디코딩_한다(TokenType tokenType) {
        // given
        JwtEncoder jwtEncoder = new JwtEncoder(tokenProperties);
        String token = jwtEncoder.encode(
                LocalDateTime.now().minusYears(3L),
                tokenType,
                "email@email.com",
                "ROLE_USER"
        );

        // when
        Optional<PrivateClaims> actual = jwtDecoder.decode(tokenType, token);

        // then
        assertThat(actual).isEmpty();
    }

    private static Stream<Arguments> encodeTestWithTokenTypeAndInvalidToken() {
        return Stream.of(
                Arguments.of(TokenType.ACCESS, null),
                Arguments.of(TokenType.ACCESS, ""),
                Arguments.of(TokenType.REFRESH, null),
                Arguments.of(TokenType.REFRESH, "")
        );
    }

    @ParameterizedTest(name = "TokenType이 {0}이고 토큰이 {1}일 때 예외가 발생한다.")
    @MethodSource("encodeTestWithTokenTypeAndInvalidToken")
    void 길이가_부족한_토큰은_디코딩_할_수_없다(TokenType tokenType, String invalidToken) {
        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(tokenType, invalidToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("토큰이 존재하지 않거나 길이가 부족합니다.");
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void 유효한_토큰을_디코딩_한다(TokenType tokenType) {
        // given
        JwtEncoder jwtEncoder = new JwtEncoder(tokenProperties);
        String id = "email@email.com";
        String roleName = "ROLE_USER";
        LocalDateTime now = LocalDateTime.now();
        String token = jwtEncoder.encode(now, tokenType, id, roleName);

        // when
        Optional<PrivateClaims> actual = jwtDecoder.decode(tokenType, token);

        // then
        assertAll(
                () -> assertThat(actual).isNotEmpty(),
                () -> assertThat(actual.get().accountId()).isEqualTo(id),
                () -> assertThat(actual.get().roleName()).isEqualTo(roleName),
                () -> assertThat(actual.get().issuedAt()).isEqualTo(now.truncatedTo(ChronoUnit.SECONDS))
        );
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void 토큰_발급자가_다른_토큰은_디코딩_할_수_없다(TokenType tokenType) {
        // given
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
        String id = "email@email.com";
        String roleName = "ROLE_USER";
        String token = jwtEncoder.encode(LocalDateTime.now(), tokenType, id, roleName);

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(tokenType, token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("서비스에서 발급한 토큰이 아닙니다.");
    }
}
