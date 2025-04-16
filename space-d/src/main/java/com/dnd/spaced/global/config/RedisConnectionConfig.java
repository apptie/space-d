package com.dnd.spaced.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!(local || test)")
@Configuration
public class RedisConnectionConfig {

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        config.useSingleServer()
              .setAddress(REDISSON_HOST_PREFIX + host + ":" + port);
        return Redisson.create(config);
    }
}
