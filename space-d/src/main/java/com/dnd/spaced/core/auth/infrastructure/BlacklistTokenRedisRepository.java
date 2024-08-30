package com.dnd.spaced.core.auth.infrastructure;

import com.dnd.spaced.core.auth.domain.BlacklistToken;
import com.dnd.spaced.core.auth.domain.BlacklistTokenRepository;
import com.dnd.spaced.global.config.properties.TokenProperties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlacklistTokenRedisRepository implements BlacklistTokenRepository {

    private static final String BLACKLIST_KEY_PREFIX = "blacklist:token:";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TokenProperties tokenProperties;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Optional<BlacklistToken> findBy(String email) {
        String registeredAt = redisTemplate.opsForValue()
                                           .get(calculateKey(email));

        if (registeredAt == null) {
            return Optional.empty();
        }

        return Optional.of(new BlacklistToken(email, LocalDateTime.parse(registeredAt, formatter)));
    }

    @Override
    public void save(BlacklistToken blacklistToken) {
        redisTemplate.opsForValue()
                     .set(
                             calculateKey(blacklistToken.getEmail()),
                             formatter.format(blacklistToken.getRegisteredAt()),
                             tokenProperties.refreshExpiredMillisSeconds(),
                             TimeUnit.MILLISECONDS
                     );
    }

    private String calculateKey(String email) {
        return BLACKLIST_KEY_PREFIX + email;
    }
}
