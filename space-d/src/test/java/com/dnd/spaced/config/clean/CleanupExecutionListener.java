package com.dnd.spaced.config.clean;

import jakarta.persistence.EntityManager;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

@Slf4j
public class CleanupExecutionListener extends AbstractTestExecutionListener implements Ordered {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        if (isNotIntegrationTest(testContext)) {
            return;
        }

        cleanupWithSql(testContext);
        cleanupWithRedis(testContext);
        cleanupWithMemoryCache(testContext);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private boolean isNotIntegrationTest(TestContext testContext) {
        return AnnotationUtils.findAnnotation(testContext.getTestClass(), SpringBootTest.class) == null;
    }

    private void cleanupWithSql(TestContext testContext) {
        DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        populator.addScript(new ClassPathResource("sql/cleanup.sql"));
        populator.execute(dataSource);
    }

    private void cleanupWithRedis(TestContext testContext) {
        RedissonConnectionFactory lettuceConnectionFactory = findLettuceConnectionFactory(testContext);
        RedisConnection redisConnection = lettuceConnectionFactory.getConnection();
        RedisServerCommands redisServerCommands = redisConnection.serverCommands();

        redisServerCommands.flushAll();
    }

    private void cleanupWithMemoryCache(TestContext testContext) {
        try {
            CacheManager cacheManager = testContext.getApplicationContext().getBean(CacheManager.class);
            cacheManager.getCacheNames().forEach(cacheName -> {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.clear();
                }
            });
        } catch (Exception e) {
            log.warn("캐시 정리 중 오류 발생", e);
        }
    }

    private RedissonConnectionFactory findLettuceConnectionFactory(TestContext testContext) {
        return testContext.getApplicationContext()
                          .getBean(RedissonConnectionFactory.class);
    }
}
