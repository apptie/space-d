package com.dnd.spaced.core.auth.infrastructure;

import com.dnd.spaced.core.auth.domain.PrivateClaims;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.domain.TokenType;
import com.dnd.spaced.core.auth.infrastructure.exception.InvalidTokenException;
import com.dnd.spaced.global.config.properties.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder implements TokenDecoder {

    private static final String CLAIM_ID = "id";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_ISSUED_AT = "iat";

    private final TokenProperties tokenProperties;

    @Override
    public Optional<PrivateClaims> decode(TokenType tokenType, String token) {
        validateToken(token);

        return this.parse(tokenType, token)
                   .map(this::convert);
    }

    private void validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("토큰이 존재하지 않거나 길이가 부족합니다.");
        }
    }

    private Optional<Claims> parse(TokenType tokenType, String token) {
        String key = tokenProperties.findTokenKey(tokenType);

        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                                .build()
                                .parseClaimsJws(token)
                                .getBody();

            validateClaims(claims);

            return Optional.of(claims);
        } catch (ExpiredJwtException ignored) {
            return Optional.empty();
        } catch (JwtException e) {
            throw new InvalidTokenException("유효한 토큰이 아닙니다.", e);
        }
    }

    private void validateClaims(Claims claims) {
        if (!tokenProperties.issuer().equals(claims.getIssuer())) {
            throw new InvalidTokenException("서비스에서 발급한 토큰이 아닙니다.");
        }
    }

    private PrivateClaims convert(Claims claims) {
        Date issuedAt = claims.get(CLAIM_ISSUED_AT, Date.class);

        return new PrivateClaims(
                claims.get(CLAIM_ID, String.class),
                claims.get(CLAIM_ROLE, String.class),
                LocalDateTime.ofInstant(issuedAt.toInstant(), ZoneId.systemDefault())
        );
    }
}
