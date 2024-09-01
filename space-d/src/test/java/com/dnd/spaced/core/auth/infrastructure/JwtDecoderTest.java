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
    void encode_메서드는_유효하지_않은_토큰이_주어지면_빈_Optional을_반환한다(TokenType tokenType) {
        // given
        String invalidToken = "Bearer abcde";

        // when
        Optional<PrivateClaims> actual = jwtDecoder.decode(tokenType, invalidToken);

        // then
        assertThat(actual).isEmpty();
    }

    @ParameterizedTest(name = "TokenType이 {0}이고 토큰이 {1}일 때 예외가 발생한다.")
    @MethodSource("encodeTestWithTokenTypeAndInvalidToken")
    void encode_메서드는_주어진_토큰이_null이거나_길이가_부족하면_InvalidTokenException_예외가_발생한다(TokenType tokenType, String invalidToken) {
        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(tokenType, invalidToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("토큰이 존재하지 않거나 길이가 부족합니다.");
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void encode_메서드는_주어진_토큰이_Bearer_타입이_아니라면_InvalidTokenException_예외가_발생한다(TokenType tokenType) {
        // given
        String invalidToken = "Basic abcdeabcde";

        // when
        assertThatThrownBy(() -> jwtDecoder.decode(tokenType, invalidToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Bearer 타입의 토큰이 아닙니다.");
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void encode_메서드는_유효한_토큰을_전달하면_토큰의_PrivateClaims를_반환한다(TokenType tokenType) {
        // given
        JwtEncoder jwtEncoder = new JwtEncoder(tokenProperties);
        String email = "email";
        String roleName = "roleName";
        LocalDateTime now = LocalDateTime.now();
        String token = jwtEncoder.encode(now, tokenType, email, roleName);

        // when
        Optional<PrivateClaims> actual = jwtDecoder.decode(tokenType, token);

        // then
        assertAll(
                () -> assertThat(actual).isNotEmpty(),
                () -> assertThat(actual.get().id()).isEqualTo(email),
                () -> assertThat(actual.get().roleName()).isEqualTo(roleName),
                () -> assertThat(actual.get().issuedAt()).isEqualTo(now.truncatedTo(ChronoUnit.SECONDS))
        );
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void encode_메서드는_issuer가_다른_토큰을_전달하면_InvalidTokenException이_발생한다(TokenType tokenType) {
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
        String email = "email";
        String roleName = "roleName";
        String token = jwtEncoder.encode(LocalDateTime.now(), tokenType, email, roleName);

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(tokenType, token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("서비스에서 발급한 토큰이 아닙니다.");
    }

    private static Stream<Arguments> encodeTestWithTokenTypeAndInvalidToken() {
        return Stream.of(
                Arguments.of(TokenType.ACCESS, null),
                Arguments.of(TokenType.ACCESS, ""),
                Arguments.of(TokenType.REFRESH, null),
                Arguments.of(TokenType.REFRESH, "")
        );
    }
}
