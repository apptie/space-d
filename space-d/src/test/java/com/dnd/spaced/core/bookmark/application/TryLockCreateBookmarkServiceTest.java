package com.dnd.spaced.core.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.exception.BookmarkLockException;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TryLockCreateBookmarkServiceTest {

    private static final Long WORD_ID = 1L;
    private static final Long ACCOUNT_ID = 1L;

    @Autowired
    CreateBookmarkService createBookmarkService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Test
    @Sql("classpath:sql/bookmark/word.sql")
    void 락_획득에_성공하면_북마크를_추가할_수_있다() {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(WORD_ID);

        // when & then
        assertDoesNotThrow(() -> createBookmarkService.createBookmark(ACCOUNT_ID, request));

        assertThat(bookmarkRepository.existsBy(ACCOUNT_ID, WORD_ID)).isTrue();
    }

    @Test
    @Sql("classpath:sql/bookmark/word.sql")
    void 락_획득에_실패하면_북마크를_추가할_수_없다() throws InterruptedException {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(WORD_ID);

        String lockName = "bookmark:create:" + WORD_ID + ":" + ACCOUNT_ID;
        RLock lock = redissonClient.getLock(lockName);

        CountDownLatch lockAcquired = new CountDownLatch(1);
        CountDownLatch testCompleted = new CountDownLatch(1);

        Thread lockHolder = new Thread(() -> {
            lock.lock();
            lockAcquired.countDown();

            try {
                testCompleted.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });

        lockHolder.start();
        lockAcquired.await();

        // when & then
        try {
            assertThatThrownBy(() -> createBookmarkService.createBookmark(ACCOUNT_ID, request))
                    .isInstanceOf(BookmarkLockException.class)
                    .hasMessage("북마크 생성 중 락 획득 실패");

            assertThat(bookmarkRepository.existsBy(ACCOUNT_ID, WORD_ID)).isFalse();
        } finally {
            testCompleted.countDown();
            lockHolder.join();
        }
    }

    @Test
    @Sql("classpath:sql/bookmark/word.sql")
    void 락_대기_중_인터럽트_발생_예외_세부_검증() throws Exception {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(WORD_ID);

        String lockName = "bookmark:create:" + WORD_ID + ":" + ACCOUNT_ID;
        RLock lock = redissonClient.getLock(lockName);

        CountDownLatch lockAcquired = new CountDownLatch(1);
        CountDownLatch serviceStarted = new CountDownLatch(1);
        CountDownLatch testCompleted = new CountDownLatch(1);

        AtomicReference<Exception> exceptionHolder = new AtomicReference<>();

        Thread lockHolder = new Thread(() -> {
            lock.lock();
            lockAcquired.countDown();
            try {
                testCompleted.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        Thread serviceThread = new Thread(() -> {
            try {
                serviceStarted.countDown();
                createBookmarkService.createBookmark(ACCOUNT_ID, request);
            } catch (Exception e) {
                exceptionHolder.set(e);
            }
        });

        lockHolder.start();
        lockAcquired.await();

        serviceThread.start();
        serviceStarted.await();

        // when
        serviceThread.interrupt();
        serviceThread.join(2000);

        // then
        Exception exception = exceptionHolder.get();

        assertAll(
                () -> assertThat(exception).isNotNull(),
                () -> assertThat(exception.getCause()).isInstanceOf(InterruptedException.class),
                () -> assertThat(bookmarkRepository.existsBy(ACCOUNT_ID, WORD_ID)).isFalse()
        );

        testCompleted.countDown();
        lockHolder.join();
    }
}
