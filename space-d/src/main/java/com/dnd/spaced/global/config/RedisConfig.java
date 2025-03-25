package com.dnd.spaced.global.config;

import com.dnd.spaced.core.word.application.event.dto.FailedWordPersistedEvent;
import com.dnd.spaced.core.word.domain.dto.PopularWord;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public ApplicationRunner redisConfigurer(RedisConnectionFactory redisConnectionFactory) {
        return args -> {
            try (RedisConnection connection = redisConnectionFactory.getConnection()) {
                connection.serverCommands().setConfig("save", "");
                connection.serverCommands().setConfig("appendonly", "no");
                connection.serverCommands().setConfig("stop-writes-on-bgsave-error", "no");
            }
        };
    }

    @Bean
    public RedisTemplate<String, PopularWord> popularWordInfoRedisTemplate() {
        RedisTemplate<String, PopularWord> popularWordInfoRedisTemplate = new RedisTemplate<>();

        popularWordInfoRedisTemplate.setConnectionFactory(redisConnectionFactory);
        popularWordInfoRedisTemplate.setKeySerializer(new StringRedisSerializer());
        popularWordInfoRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return popularWordInfoRedisTemplate;
    }
    
    @Bean
    public RedisTemplate<String, Long> popularWordIdRedisTemplate() {
        RedisTemplate<String, Long> popularWordIdRedisTemplate = new RedisTemplate<>();

        popularWordIdRedisTemplate.setConnectionFactory(redisConnectionFactory);
        popularWordIdRedisTemplate.setKeySerializer(new StringRedisSerializer());
        popularWordIdRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        return popularWordIdRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, Long> likeCountRedisTemplate() {
        RedisTemplate<String, Long> likeCountRedisTemplate = new RedisTemplate<>();

        likeCountRedisTemplate.setConnectionFactory(redisConnectionFactory);
        likeCountRedisTemplate.setKeySerializer(new StringRedisSerializer());
        likeCountRedisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(Long.class));
        likeCountRedisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Integer.class));

        return likeCountRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, FailedWordPersistedEvent> deadLetterQueueRedisTemplate() {
        RedisTemplate<String, FailedWordPersistedEvent> deadLetterQueueRedisTemplate = new RedisTemplate<>();

        deadLetterQueueRedisTemplate.setConnectionFactory(redisConnectionFactory);
        deadLetterQueueRedisTemplate.setKeySerializer(new StringRedisSerializer());
        deadLetterQueueRedisTemplate.setValueSerializer(
                new Jackson2JsonRedisSerializer<>(FailedWordPersistedEvent.class)
        );
        return deadLetterQueueRedisTemplate;
    }
}
