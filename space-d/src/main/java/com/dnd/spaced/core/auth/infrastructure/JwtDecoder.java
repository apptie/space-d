package com.dnd.spaced.core.auth.infrastructure;

import com.dnd.spaced.core.auth.domain.PrivateClaims;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.domain.TokenType;
import com.dnd.spaced.core.auth.infrastructure.exception.InvalidTokenException;
import com.dnd.spaced.global.config.properties.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder implements TokenDecoder {

    private static final String CLAIM_ID = "id";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_ISSUED_AT = "iat";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final TokenProperties tokenProperties;

    @Override
    public Optional<PrivateClaims> decode(TokenType tokenType, String token) {
        validateBearerToken(token);

        return this.parse(tokenType, token)
                   .map(this::convert);
    }

    private void validateBearerToken(String token) {
        try {
            String tokenPrefix = token.substring(0, BEARER_TOKEN_PREFIX.length());

            validateTokenType(tokenPrefix);
        } catch (StringIndexOutOfBoundsException | NullPointerException e) {
            throw new InvalidTokenException("토큰이 존재하지 않거나 길이가 부족합니다.", e);
        }
    }

    private void validateTokenType(String tokenPrefix) {
        if (!BEARER_TOKEN_PREFIX.equals(tokenPrefix)) {
            throw new InvalidTokenException("Bearer 타입의 토큰이 아닙니다.");
        }
    }

    private Optional<Claims> parse(TokenType tokenType, String token) {
        String key = tokenProperties.findTokenKey(tokenType);

        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                                .build()
                                .parseClaimsJws(findPureToken(token))
                                .getBody();

            validateClaims(claims);

            return Optional.of(claims);
        } catch (JwtException ignored) {
            return Optional.empty();
        }
    }

    private void validateClaims(Claims claims) {
        if (!tokenProperties.issuer().equals(claims.getIssuer())) {
            throw new InvalidTokenException("서비스에서 발급한 토큰이 아닙니다.");
        }
    }

    private String findPureToken(String token) {
        return token.substring(BEARER_TOKEN_PREFIX.length());
    }

    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(
                claims.get(CLAIM_ID, String.class),
                claims.get(CLAIM_ROLE, String.class)
        );
    }
}
