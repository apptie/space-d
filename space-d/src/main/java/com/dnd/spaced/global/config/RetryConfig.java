package com.dnd.spaced.global.config;

import com.dnd.spaced.global.exception.base.BaseClientException;
import com.dnd.spaced.global.exception.base.BaseServerException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Profile("!test")
@EnableRetry
@Configuration
public class RetryConfig {

    @Bean
    public RetryTemplate wordPersistEventRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        Map<Class<? extends Throwable>, Boolean> targetException = new HashMap<>();
        targetException.put(BaseServerException.class, true);
        targetException.put(BaseClientException.class, true);
        targetException.put(DataAccessException.class, true);

        RetryPolicy retryPolicy = new SimpleRetryPolicy(3, targetException);
        retryTemplate.setRetryPolicy(retryPolicy);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1_000L);
        backOffPolicy.setMultiplier(2.0d);
        backOffPolicy.setMaxInterval(10_000L);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    @Bean
    public RetryTemplate gradedQuizRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        Map<Class<? extends Throwable>, Boolean> targetException = new HashMap<>();
        targetException.put(BaseServerException.class, true);

        RetryPolicy retryPolicy = new SimpleRetryPolicy(3, targetException);
        retryTemplate.setRetryPolicy(retryPolicy);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1_000L);
        backOffPolicy.setMultiplier(2.0d);
        backOffPolicy.setMaxInterval(10_000L);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}
