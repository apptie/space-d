package com.dnd.spaced.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class AsyncThreadPoolTestConfig {

    @Bean(name = "asyncQuizMetadataCounterExecutor")
    @Primary
    public Executor asyncQuizMetadataCounterExecutor() {
        return Runnable::run;
    }

    @Bean(name = "asyncCalculateSkillExecutor")
    @Primary
    public Executor asyncCalculateSkillExecutor() {
        return Runnable::run;
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
}
