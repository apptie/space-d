package com.dnd.spaced.core.auth.infrastructure.jwt;

import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.dnd.spaced.global.auth.encryptor.Encryptor;
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

    private static final String CLAIM_ID = "id";
    private static final String CLAIM_ROLE = "role";

    private final Encryptor encryptor;
    private final TokenProperties tokenProperties;

    @Override
    public String encode(LocalDateTime publishTime, TokenType tokenType, Long accountId, String roleName) {
        Date targetDate = convertDate(publishTime);
        String key = tokenProperties.findTokenKey(tokenType);
        Long expiredMillisSeconds = tokenProperties.findExpiredMillisSeconds(tokenType);
        Map<String, Object> attributes = Map.of(CLAIM_ID, accountId, CLAIM_ROLE, roleName);
        String token = Jwts.builder()
                             .setIssuer(tokenProperties.issuer())
                             .setIssuedAt(targetDate)
                             .setExpiration(new Date(targetDate.getTime() + expiredMillisSeconds))
                             .addClaims(attributes)
                             .signWith(
                                     Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)),
                                     SignatureAlgorithm.HS256
                             )
                             .compact();

        return encryptor.encrypt(token);
    }

    private Date convertDate(LocalDateTime target) {
        Instant targetInstant = target.atZone(ZoneId.systemDefault())
                                      .toInstant();

        return Date.from(targetInstant);
    }
}
