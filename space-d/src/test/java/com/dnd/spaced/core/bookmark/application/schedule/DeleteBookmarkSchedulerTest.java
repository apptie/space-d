package com.dnd.spaced.core.bookmark.application.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import com.dnd.spaced.core.word.application.DeletedWordIdRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.MockUtil;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeleteBookmarkSchedulerTest {

    @Autowired
    DeleteBookmarkScheduler deleteBookmarkScheduler;

    @Autowired
    DeletedWordIdRepository deletedWordIdRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    Clock clock;

    @Test
    @Sql(scripts = {
            "classpath:sql/bookmark/deleted_word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 삭제한_용어에_등록된_북마크를_삭제한다() {
        // given
        doReturn(Set.of(1L)).when(deletedWordIdRepository).findAllBy(any(LocalDateTime.class));

        // when
        deleteBookmarkScheduler.schedule();

        // then
        assertAll(
                () -> verify(deletedWordIdRepository).findAllBy(any(LocalDateTime.class)),
                () -> verify(bookmarkRepository).deleteAllBy(any())
        );
    }

    @Test
    void 북마크_삭제_스케줄러가_실패하더라도_최대_재시도_횟수만큼_이벤트_처리를_재시도한다() {
        // given
        doThrow(DataAccessResourceFailureException.class).doReturn(Set.of(1L))
                                                         .when(deletedWordIdRepository)
                                                         .findAllBy(any(LocalDateTime.class));

        // when
        deleteBookmarkScheduler.schedule();

        // then
        assertAll(
                () -> verify(deletedWordIdRepository, times(2)).findAllBy(any(LocalDateTime.class)),
                () -> verify(bookmarkRepository).deleteAllBy(any())
        );
    }

    @Test
    void 북마크_삭제_스케줄러_처리_시_최대_재시도_횟수보다_더_실패한_횟수가_많다면_로그를_출력한다() {
        // given
        Logger logger = (Logger) LoggerFactory.getLogger(DeleteBookmarkScheduler.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();

        appender.start();
        logger.addAppender(appender);

        doThrow(DataAccessResourceFailureException.class).doThrow(DataAccessResourceFailureException.class)
                                                         .doThrow(DataAccessResourceFailureException.class)
                                                         .when(deletedWordIdRepository)
                                                         .findAllBy(any(LocalDateTime.class));

        // when
        deleteBookmarkScheduler.schedule();

        // then
        List<ILoggingEvent> logs = appender.list;

        assertAll(
                () -> verify(deletedWordIdRepository, times(3)).findAllBy(any(LocalDateTime.class)),
                () -> verify(bookmarkRepository, never()).deleteAllBy(any()),
                () -> assertThat(logs).hasSize(1),
                () -> assertThat(logs.get(0).getLevel()).isEqualTo(Level.ERROR),
                () -> assertThat(logs.get(0).getFormattedMessage()).contains("[DeleteBookmarkScheduler.schedule]")
                                                                   .contains("용어 삭제로 인한 북마크 삭제 이벤트 처리 실패")
        );
    }
}
