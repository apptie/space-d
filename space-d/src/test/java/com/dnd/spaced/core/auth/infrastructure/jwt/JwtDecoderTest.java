package com.dnd.spaced.core.auth.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.auth.domain.PrivateClaims;
import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.dnd.spaced.core.auth.infrastructure.jwt.exception.InvalidTokenException;
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
    void 유효하지_않은_길이의_토큰을_인코딩_할_수_없다(TokenType tokenType) {
        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(tokenType, "Bearer invalid"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("유효한 토큰이 아닙니다.");
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void 만료된_토큰을_디코딩_하면_빈_claim을_반환한다(TokenType tokenType) {
        // given
        JwtEncoder jwtEncoder = new JwtEncoder(tokenProperties);
        String token = jwtEncoder.encode(
                LocalDateTime.of(2022, 2, 2, 13, 13),
                tokenType,
                1L,
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

    @ParameterizedTest(name = "TokenType이 {0}이고 토큰이 {1}일 때 토큰 디코딩을 할 수 없다")
    @MethodSource("encodeTestWithTokenTypeAndInvalidToken")
    void 비어_있는_토큰은_디코딩_할_수_없다(TokenType tokenType, String invalidToken) {
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
        LocalDateTime publishTime = LocalDateTime.now();
        String token = jwtEncoder.encode(publishTime, tokenType, 1L, "ROLE_USER");

        // when
        Optional<PrivateClaims> actual = jwtDecoder.decode(tokenType, token);

        // then
        assertAll(
                () -> assertThat(actual).isNotEmpty(),
                () -> assertThat(actual.get().accountId()).isEqualTo(1L),
                () -> assertThat(actual.get().roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(actual.get().issuedAt()).isEqualTo(publishTime.truncatedTo(ChronoUnit.SECONDS))
        );
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void 토큰_발급자가_다른_토큰은_디코딩_할_수_없다(TokenType tokenType) {
        // given
        TokenProperties otherIssuerTokenProperties = new TokenProperties(
                "thisistoolargeaccesstokenkeyfordummykeydatafortest",
                "thisistoolargerefreshtokenkeyfordummykeydatafortest",
                "other-issuer",
                43200,
                259200,
                43200000L,
                259200000L
        );
        JwtEncoder jwtEncoder = new JwtEncoder(otherIssuerTokenProperties);
        String token = jwtEncoder.encode(LocalDateTime.now(), tokenType, 1L, "ROLE_USER");

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(tokenType, token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("서비스에서 발급한 토큰이 아닙니다.");
    }
}
