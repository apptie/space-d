package com.dnd.spaced.global.config;

import com.dnd.spaced.global.consts.CacheConst;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Primary
    public CacheManager memoryCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CacheConst.TODAY_QUIZ_CACHE_NAME);

        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(1)
        );
        return cacheManager;
    }

    @Bean
    public CacheManager oidcCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                                       .serializeKeysWith(
                                               RedisSerializationContext.SerializationPair.fromSerializer(
                                                       new StringRedisSerializer()
                                               )
                                       )
                                       .serializeValuesWith(
                                               RedisSerializationContext.SerializationPair.fromSerializer(
                                                       new GenericJackson2JsonRedisSerializer()
                                               )
                                       )
                                       .entryTtl(Duration.ofDays(7L));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                                                         .cacheDefaults(redisCacheConfiguration)
                                                         .build();
    }
}
