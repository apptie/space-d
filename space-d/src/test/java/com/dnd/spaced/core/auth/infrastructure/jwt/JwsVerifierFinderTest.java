package com.dnd.spaced.core.auth.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.nimbusds.jose.JWSVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwsVerifierFinderTest {

    JWSVerifier accessTokenJwsVerifier;
    JWSVerifier refreshTokenJwsVerifier;
    JwsVerifierFinder jwsVerifierFinder;

    @BeforeEach
    void beforeEach() {
        accessTokenJwsVerifier = mock(JWSVerifier.class);
        refreshTokenJwsVerifier = mock(JWSVerifier.class);
        jwsVerifierFinder = new JwsVerifierFinder(accessTokenJwsVerifier, refreshTokenJwsVerifier);
    }
    
    @Test
    void accessToken을_위한_verifier를_조회한다() {
        // when
        JWSVerifier actual = jwsVerifierFinder.findByTokenType(TokenType.ACCESS);

        // then
        assertThat(actual).isSameAs(accessTokenJwsVerifier);
    }

    @Test
    void refreshToken을_위한_verifier를_조회한다() {
        // when
        JWSVerifier actual = jwsVerifierFinder.findByTokenType(TokenType.REFRESH);

        // then
        assertThat(actual).isSameAs(refreshTokenJwsVerifier);
    }
}
