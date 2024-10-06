package com.dnd.spaced.core.like.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.core.like.infrastructure.dto.LikeCountIdentifier;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.RepeatedTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LikeCountBufferTest {

    @RepeatedTest(10)
    @SuppressWarnings("unchecked")
    void addLikeCount_메서드는_임계치에_도달하는_타이밍에_동시에_두_개의_스레드가_접근하더라도_flush를_한_번만_수행한다() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        // given
        CountDownLatch callbackRunCountDownLatch = new CountDownLatch(1);
        AtomicInteger callbackRunCounter = new AtomicInteger(0);
        ExecutorService testThreadPool = Executors.newFixedThreadPool(10);
        ExecutorService bufferThreadPool = Executors.newFixedThreadPool(2);
        AtomicInteger bufferSizeBeforeFlush = new AtomicInteger();
        LikeCountBuffer buffer = new LikeCountBuffer(map -> {
            bufferSizeBeforeFlush.set(map.size());
            callbackRunCounter.incrementAndGet();
            callbackRunCountDownLatch.countDown();
        }, bufferThreadPool);

        for (long i = 0L; i < 99L; i++) {
            buffer.addLikeCount(new LikeCountIdentifier(i, i));
        }

        // when
        CountDownLatch testCountDownLatch = new CountDownLatch(10);

        Runnable addLikeCountTask1 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(101L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask2 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(102L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask3 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(103L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask4 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(104L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask5 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(105L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask6 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(106L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask7 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(107L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask8 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(108L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask9 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(109L, 100L));
            testCountDownLatch.countDown();
        };
        Runnable addLikeCountTask10 = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(110L, 100L));
            testCountDownLatch.countDown();
        };

        testThreadPool.execute(addLikeCountTask1);
        testThreadPool.execute(addLikeCountTask2);
        testThreadPool.execute(addLikeCountTask3);
        testThreadPool.execute(addLikeCountTask4);
        testThreadPool.execute(addLikeCountTask5);
        testThreadPool.execute(addLikeCountTask6);
        testThreadPool.execute(addLikeCountTask7);
        testThreadPool.execute(addLikeCountTask8);
        testThreadPool.execute(addLikeCountTask9);
        testThreadPool.execute(addLikeCountTask10);

        testCountDownLatch.await();
        callbackRunCountDownLatch.await();

        // then
        Field currentBufferField = LikeCountBuffer.class.getDeclaredField("currentBuffer");
        currentBufferField.setAccessible(true);
        AtomicReference<Map<LikeCountIdentifier, Integer>> currentBuffer = (AtomicReference<Map<LikeCountIdentifier, Integer>>) currentBufferField.get(buffer);
        int bufferSizeAfterFlush = currentBuffer.get().size();

        assertThat(callbackRunCounter.get()).isOne();
        assertThat(bufferSizeBeforeFlush.get() + bufferSizeAfterFlush).isEqualTo(109);
    }
}
