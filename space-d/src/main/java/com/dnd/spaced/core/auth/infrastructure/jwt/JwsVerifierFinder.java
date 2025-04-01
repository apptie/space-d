package com.dnd.spaced.core.auth.infrastructure.jwt;

import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.nimbusds.jose.JWSVerifier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwsVerifierFinder {

    private final JWSVerifier accessTokenJwsVerifier;
    private final JWSVerifier refreshTokenJwsVerifier;

    public JWSVerifier findByTokenType(TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessTokenJwsVerifier;
        }

        return refreshTokenJwsVerifier;
    }
}
