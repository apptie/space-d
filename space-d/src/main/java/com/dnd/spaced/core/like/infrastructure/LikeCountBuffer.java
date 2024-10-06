package com.dnd.spaced.core.like.infrastructure;

import com.dnd.spaced.core.like.infrastructure.dto.LikeCountIdentifier;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeCountBuffer {

    private static final int FLUSH_THRESHOLD = 100;

    private final AtomicReference<Map<LikeCountIdentifier, Integer>> currentBuffer = new AtomicReference<>(new ConcurrentHashMap<>());
    private final Consumer<Map<LikeCountIdentifier, Integer>> cacheUpdateCallback;
    private final Executor asyncCommentLikeCountExecutor;

    public void addLikeCount(LikeCountIdentifier identifier) {
        Map<LikeCountIdentifier, Integer> buffer = currentBuffer.get();
        buffer.merge(identifier, 1, Integer::sum);

        if (buffer.size() == FLUSH_THRESHOLD) {
            flushBuffer();
        }
    }

    public void deleteLikeCount(LikeCountIdentifier identifier) {
        currentBuffer.get()
                     .merge(identifier, -1, Integer::sum);
    }

    private void flushBuffer() {
        Map<LikeCountIdentifier, Integer> buffer = currentBuffer.getAndSet(new ConcurrentHashMap<>());

        CompletableFuture.runAsync(() -> cacheUpdateCallback.accept(buffer), asyncCommentLikeCountExecutor);
    }
}
