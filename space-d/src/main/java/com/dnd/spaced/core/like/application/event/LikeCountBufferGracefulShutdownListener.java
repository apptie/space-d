package com.dnd.spaced.core.like.application.event;

import com.dnd.spaced.core.like.infrastructure.LikeCountRedisRepository;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeCountBufferGracefulShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private static final long SHUTDOWN_TIMEOUT_SECOND = 60;

    private final LikeCountRedisRepository likeCountRedisRepository;

    @Override
    public void onApplicationEvent(ContextClosedEvent ignored) {
        CompletableFuture<Void> shutdownFuture = CompletableFuture.runAsync(
                                                                          likeCountRedisRepository::flushBuffer,
                                                                          Executors.newCachedThreadPool()
                                                                  )
                                                                  .orTimeout(SHUTDOWN_TIMEOUT_SECOND, TimeUnit.SECONDS);

        shutdownFuture.join();
    }
}
