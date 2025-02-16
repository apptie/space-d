package com.dnd.spaced.config.spy;

import com.dnd.spaced.core.like.infrastructure.LikeCountBuffer;
import com.dnd.spaced.core.like.infrastructure.dto.LikeCountIdentifier;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SpyLikeCountBuffer extends LikeCountBuffer {

    private AtomicInteger addLikeCountCallCount = new AtomicInteger(0);
    private AtomicInteger deleteLikeCountCallCount = new AtomicInteger(0);
    private AtomicInteger flushBufferCallCount = new AtomicInteger(0);

    public SpyLikeCountBuffer(
            Consumer<Map<LikeCountIdentifier, Integer>> cacheUpdateCallback,
            Executor asyncCommentLikeCountExecutor) {
        super(cacheUpdateCallback, asyncCommentLikeCountExecutor);
    }

    @Override
    public void addLikeCount(LikeCountIdentifier identifier) {
        addLikeCountCallCount.incrementAndGet();
        super.addLikeCount(identifier);
    }

    @Override
    public void deleteLikeCount(LikeCountIdentifier identifier) {
        deleteLikeCountCallCount.incrementAndGet();
        super.deleteLikeCount(identifier);
    }

    @Override
    public void flushBuffer() {
        flushBufferCallCount.incrementAndGet();
        super.flushBuffer();
    }

    public int getAddLikeCountCallCount() {
        return addLikeCountCallCount.get();
    }

    public int getDeleteLikeCountCallCount() {
        return deleteLikeCountCallCount.get();
    }

    public int getFlushBufferCallCount() {
        return flushBufferCallCount.get();
    }
}
