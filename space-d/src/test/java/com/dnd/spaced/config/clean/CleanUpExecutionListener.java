package com.dnd.spaced.config.clean;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

@Slf4j
public class CleanUpExecutionListener extends AbstractTestExecutionListener implements Ordered {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        if (isNotIntegrationTest(testContext)) {
            return;
        }

        cleanUpWithSql(testContext);
        cleanUpWithRedis(testContext);
    }

    @Override
    public int getOrder() {
        return 4999;
    }

    private boolean isNotIntegrationTest(TestContext testContext) {
        return AnnotationUtils.findAnnotation(testContext.getTestClass(), SpringBootTest.class) == null;
    }

    private void cleanUpWithSql(TestContext testContext) {
        DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        populator.addScript(new ClassPathResource("sql/cleanup.sql"));
        populator.execute(dataSource);
    }

    private void cleanUpWithRedis(TestContext testContext) {
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
