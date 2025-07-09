package com.dnd.spaced.core.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.DeleteBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.application.exception.AlreadyExistsBookmarkException;
import com.dnd.spaced.core.bookmark.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountDecrementedEvent;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountIncrementedEvent;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookmarkServiceTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    BookmarkService bookmarkService;

    @Test
    @Sql("classpath:sql/bookmark/word.sql")
    void 북마크를_추가한다() {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(1L);

        // when
        bookmarkService.createBookmark(1L, request);

        // then
        assertThat(events.stream(WordBookmarkCountIncrementedEvent.class).count()).isOne();
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 이미_북마크에_추가된_용어를_북마에_추가할_수_없다() {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(1L);

        // when & then
        assertThatThrownBy(() -> bookmarkService.createBookmark(1L, request))
                .isInstanceOf(AlreadyExistsBookmarkException.class)
                .hasMessage("이미 북마크에 추가된 용어입니다.");
    }

    @Test
    void 지정한_용어_식별자로_용어를_찾지_못하면_북마크를_추가할_수_없다() {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(-999L);

        // when & then
        assertThatThrownBy(() -> bookmarkService.createBookmark(1L, request))
                .isInstanceOf(WordNotFoundException.class)
                .hasMessage("지정한 식별자의 용어를 찾지 못했습니다.");

    }

    @Test
    @Sql("classpath:sql/bookmark/bookmark.sql")
    void 북마크를_삭제한다() {
        // given
        DeleteBookmarkRequest request = new DeleteBookmarkRequest(1L);

        // when
        bookmarkService.deleteBookmark(1L, request);

        // then
        assertThat(events.stream(WordBookmarkCountDecrementedEvent.class).count()).isOne();
    }

    @Test
    @Sql(value = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 회원이_생성한_북마크를_모두_조회한다() {
        // given
        ReadAllBookmarkRequest request = new ReadAllBookmarkRequest(null);

        // when
        BookmarkCollectionResponse actual = bookmarkService.readBookmarks(1L, request, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(actual.bookmarks()).hasSize(1),
                () -> assertThat(actual.lastBookmarkId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).bookmarkId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).accountId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).wordId()).isEqualTo(1L)
        );
    }
}
