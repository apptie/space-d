package com.dnd.spaced.core.auth.infrastructure;

import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.domain.TokenType;
import com.dnd.spaced.global.config.properties.TokenProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtEncoder implements TokenEncoder {

    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final TokenProperties tokenProperties;

    @Override
    public String encode(LocalDateTime publishTime, TokenType tokenType, String email, String roleName) {
        Date targetDate = convertDate(publishTime);
        String key = tokenProperties.findTokenKey(tokenType);
        Long expiredMillisSeconds = tokenProperties.findExpiredMillisSeconds(tokenType);
        Map<String, Object> attributes = Map.of(CLAIM_EMAIL, email, CLAIM_ROLE, roleName);

        return BEARER_TOKEN_PREFIX + Jwts.builder()
                                         .setIssuer(tokenProperties.issuer())
                                         .setIssuedAt(targetDate)
                                         .setExpiration(new Date(targetDate.getTime() + expiredMillisSeconds))
                                         .addClaims(attributes)
                                         .signWith(
                                                 Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)),
                                                 SignatureAlgorithm.HS256
                                         )
                                         .compact();
    }

    private Date convertDate(LocalDateTime target) {
        Instant targetInstant = target.atZone(ZoneId.systemDefault())
                                      .toInstant();

        return Date.from(targetInstant);
    }
}
