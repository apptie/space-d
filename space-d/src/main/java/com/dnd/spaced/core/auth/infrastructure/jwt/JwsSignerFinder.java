package com.dnd.spaced.core.auth.infrastructure.jwt;

import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.nimbusds.jose.JWSSigner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwsSignerFinder {

    private final JWSSigner accessTokenSigner;
    private final JWSSigner refreshTokenSigner;

    public JWSSigner findByTokenType(TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessTokenSigner;
        }

        return refreshTokenSigner;
    }
}
