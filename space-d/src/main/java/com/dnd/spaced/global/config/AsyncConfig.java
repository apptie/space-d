package com.dnd.spaced.global.config;

import com.dnd.spaced.global.log.MDCTaskDecorator;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Profile("!test")
@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean
    public Executor asyncStatisticsWordViewCountExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setKeepAliveSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setAwaitTerminationSeconds(30);
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }
    
    @Bean
    public Executor asyncCalculateSkillExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setKeepAliveSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setAwaitTerminationSeconds(30);
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor asyncWordViewCounterExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setKeepAliveSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setAwaitTerminationSeconds(30);
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor asyncQuizMetadataCounterExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setKeepAliveSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setAwaitTerminationSeconds(30);
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor asyncBookmarkCounterExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setKeepAliveSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setAwaitTerminationSeconds(30);
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor asyncPersistedWordEventListenerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setKeepAliveSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setAwaitTerminationSeconds(30);
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor asyncCommentLikeCountExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int corePoolSize = Math.max(1, availableProcessors / 6);
        int maxPoolSize = Math.max(2, corePoolSize * 2);

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(10);
        executor.setKeepAliveSeconds(180);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setAwaitTerminationSeconds(20);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();

        return executor;
    }
}
