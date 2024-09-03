package com.dnd.spaced.config.clean;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class CleanUpRedisListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        LettuceConnectionFactory lettuceConnectionFactory = findLettuceConnectionFactory(testContext);
        RedisConnection redisConnection = lettuceConnectionFactory.getConnection();
        RedisServerCommands redisServerCommands = redisConnection.serverCommands();

        redisServerCommands.flushAll();
    }

    private LettuceConnectionFactory findLettuceConnectionFactory(TestContext testContext) {
        return testContext.getApplicationContext()
                          .getBean(LettuceConnectionFactory.class);
    }
}
