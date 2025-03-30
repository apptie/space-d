package com.dnd.spaced.core.auth.infrastructure.jwt;

import com.dnd.spaced.core.auth.domain.PrivateClaims;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.dnd.spaced.core.auth.infrastructure.jwt.exception.InvalidTokenException;
import com.dnd.spaced.global.auth.encryptor.Encryptor;
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

    private final Encryptor encryptor;
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

    private Optional<Claims> parse(TokenType tokenType, String cipherToken) {
        String key = tokenProperties.findTokenKey(tokenType);

        try {
            String decryptToken = encryptor.decrypt(cipherToken);
            Claims claims = parseJwtToken(decryptToken, key);

            validateIssuer(claims);

            return Optional.of(claims);
        } catch (ExpiredJwtException ignored) {
            return Optional.empty();
        } catch (JwtException e) {
            throw new InvalidTokenException("유효한 토큰이 아닙니다.", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Claims parseJwtToken(String token, String key) {
        return Jwts.parserBuilder()
                   .setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    private void validateIssuer(Claims claims) {
        if (!tokenProperties.issuer().equals(claims.getIssuer())) {
            throw new InvalidTokenException("서비스에서 발급한 토큰이 아닙니다.");
        }
    }

    private PrivateClaims convert(Claims claims) {
        Date issuedAt = claims.get(CLAIM_ISSUED_AT, Date.class);

        return new PrivateClaims(
                claims.get(CLAIM_ID, Long.class),
                claims.get(CLAIM_ROLE, String.class),
                LocalDateTime.ofInstant(issuedAt.toInstant(), ZoneId.systemDefault())
        );
    }
}
