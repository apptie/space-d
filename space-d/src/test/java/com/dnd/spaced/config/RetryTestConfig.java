package com.dnd.spaced.config;

import com.dnd.spaced.global.exception.base.BaseClientException;
import com.dnd.spaced.global.exception.base.BaseServerException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Profile("test")
@Configuration
public class RetryTestConfig {

    @Bean
    public RetryTemplate repositoryRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        Map<Class<? extends Throwable>, Boolean> targetException = new HashMap<>();
        targetException.put(DataAccessResourceFailureException.class, true);
        targetException.put(TransientDataAccessException.class, true);
        targetException.put(QueryTimeoutException.class, true);

        RetryPolicy retryPolicy = new SimpleRetryPolicy(3, targetException);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
