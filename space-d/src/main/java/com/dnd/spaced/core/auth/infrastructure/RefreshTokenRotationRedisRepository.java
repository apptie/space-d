package com.dnd.spaced.core.auth.infrastructure;

import com.dnd.spaced.core.auth.domain.repository.RefreshTokenRotationRepository;
import com.dnd.spaced.global.config.properties.TokenProperties;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRotationRedisRepository implements RefreshTokenRotationRepository {

    private static final String RTT_KEY_PREFIX = "rtt:";

    private final TokenProperties tokenProperties;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String accountId, String refreshToken) {
        redisTemplate.opsForValue()
                     .set(
                             calculateKey(accountId),
                             refreshToken,
                             tokenProperties.refreshExpiredMillisSeconds(),
                             TimeUnit.MILLISECONDS
                     );
    }

    @Override
    public Optional<String> findBy(String accountId) {
        String refreshToken = redisTemplate.opsForValue()
                                           .get(calculateKey(accountId));

        if (refreshToken == null) {
            return Optional.empty();
        }

        return Optional.of(refreshToken);
    }

    private String calculateKey(String accountId) {
        return RTT_KEY_PREFIX + accountId;
    }
}
