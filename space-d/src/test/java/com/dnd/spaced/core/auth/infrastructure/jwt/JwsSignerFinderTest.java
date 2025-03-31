package com.dnd.spaced.core.auth.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.nimbusds.jose.JWSSigner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwsSignerFinderTest {

    JWSSigner accessTokenJwsSigner;
    JWSSigner refreshTokenJwsSigner;
    JwsSignerFinder jwsSignerFinder;

    @BeforeEach
    void beforeEach() {
        accessTokenJwsSigner = mock(JWSSigner.class);
        refreshTokenJwsSigner = mock(JWSSigner.class);
        jwsSignerFinder = new JwsSignerFinder(accessTokenJwsSigner, refreshTokenJwsSigner);
    }

    @Test
    void accessToken을_위한_signer를_조회한다() {
        // when
        JWSSigner actual = jwsSignerFinder.findByTokenType(TokenType.ACCESS);

        // then
        assertThat(actual).isSameAs(accessTokenJwsSigner);
    }

    @Test
    void refreshToken을_위한_signer를_조회한다() {
        // when
        JWSSigner actual = jwsSignerFinder.findByTokenType(TokenType.REFRESH);

        // then
        assertThat(actual).isSameAs(refreshTokenJwsSigner);
    }
}
