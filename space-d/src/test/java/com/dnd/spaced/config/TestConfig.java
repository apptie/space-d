package com.dnd.spaced.config;

import com.dnd.spaced.config.stub.StubNicknameProperties;
import com.dnd.spaced.global.config.properties.NicknameProperties;
import com.dnd.spaced.global.exception.base.BaseServerException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Profile("test")
@Configuration
public class TestConfig {

    @Bean
    @Primary
    public NicknameProperties nicknameProperties() {
        return new StubNicknameProperties();
    }

    @Bean(name = "asyncQuizMetadataCounterExecutor")
    @Primary
    public Executor asyncQuizMetadataCounterExecutor() {
        return Runnable::run;
    }

    @Bean(name = "asyncCalculateSkillExecutor")
    @Primary
    public Executor asyncCalculateSkillExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean(name = "asyncWordViewCounterExecutor")
    @Primary
    public Executor asyncWordViewCounterExecutor() {
        return Runnable::run;
    }

    @Bean(name = "asyncBookmarkCounterExecutor")
    @Primary
    public Executor asyncBookmarkCounterExecutor() {
        return Runnable::run;
    }

    @Bean(name = "asyncStatisticsWordViewCountExecutor")
    @Primary
    public Executor asyncStatisticsWordViewCountExecutor() {
        return Runnable::run;
    }

    @Bean(name = "asyncPersistedWordEventListenerExecutor")
    @Primary
    public Executor asyncPersistedWordEventListenerExecutor() {
        return Runnable::run;
    }

    @Bean(name = "wordPersistEventRetryTemplate")
    @Primary
    public RetryTemplate wordPersistEventRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        Map<Class<? extends Throwable>, Boolean> targetException = new HashMap<>();
        targetException.put(BaseServerException.class, true);

        RetryPolicy retryPolicy = new SimpleRetryPolicy(3, targetException);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
