package com.dnd.spaced.core.like.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.core.like.infrastructure.dto.LikeCountIdentifier;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.RepeatedTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LikeCountBufferTest {

    AtomicInteger callCount = new AtomicInteger(0);
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    LikeCountBuffer buffer;

    @BeforeEach
    void setUp() {
        this.buffer = new LikeCountBuffer(ignored -> callCount.incrementAndGet(), executorService);
    }

    @RepeatedTest(50)
    void addLikeCount_메서드는_임계치에_도달하는_타이밍에_동시에_두_개의_스레드가_접근하더라도_flush를_한_번만_수행한다() throws InterruptedException {
        // given
        for (long i = 0L; i < 99L; i++) {
            buffer.addLikeCount(new LikeCountIdentifier(i, i));
        }

        // when
        CountDownLatch latch = new CountDownLatch(2);

        Runnable addLikeCountTask = () -> {
            buffer.addLikeCount(new LikeCountIdentifier(1L, 100L));
            latch.countDown();
        };

        executorService.execute(addLikeCountTask);
        executorService.execute(addLikeCountTask);

        latch.await();

        // then
        assertThat(callCount.get()).isOne();
    }
}