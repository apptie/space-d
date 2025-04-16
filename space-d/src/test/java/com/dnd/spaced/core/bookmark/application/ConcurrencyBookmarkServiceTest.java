package com.dnd.spaced.core.bookmark.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.domain.Bookmark;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ConcurrencyBookmarkServiceTest {

    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Test
    @Sql("classpath:sql/bookmark/word.sql")
    void 동시에_동일한_용어에_북마크_생성_요청을_하더라도_단_하나의_북마크만_생성되어야_한다() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        CreateBookmarkRequest request = new CreateBookmarkRequest(1L);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    latch.countDown();
                    latch.await();

                    bookmarkService.createBookmark(1L, request);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            Thread.sleep(100);
        }

        assertAll(
                () -> verify(bookmarkRepository, times(10)).existsBy(anyLong(), anyLong()),
                () -> verify(bookmarkRepository).save(any(Bookmark.class))
        );
    }
}
