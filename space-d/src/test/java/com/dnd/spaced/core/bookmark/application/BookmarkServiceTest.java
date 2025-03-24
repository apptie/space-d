package com.dnd.spaced.core.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.DeleteBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.application.exception.WordNotFoundException;
import com.dnd.spaced.core.bookmark.application.helper.WithWordTestHelper;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanUpDatabase
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookmarkServiceTest extends WithWordTestHelper {

    @Autowired
    ApplicationEvents events;

    @Autowired
    BookmarkService bookmarkService;

    @Test
    void 북마크를_추가한다() {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(word.getId());

        // when

        bookmarkService.createBookmark(1L, request);

        // then
        assertThat(events.stream(WordBookmarkCountIncrementedEvent.class).count()).isOne();
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
    void 북마크를_삭제한다() {
        // given
        bookmarkService.createBookmark(1L, new CreateBookmarkRequest(word.getId()));
        DeleteBookmarkRequest request = new DeleteBookmarkRequest(1L);

        // when
        bookmarkService.deleteBookmark(1L, request);

        // then
        assertThat(events.stream(WordBookmarkCountDecrementedEvent.class).count()).isOne();
    }

    @Test
    void 회원이_생성한_북마크를_모두_조회한다() {
        // given
        bookmarkService.createBookmark(1L, new CreateBookmarkRequest(word.getId()));

        // when
        ReadAllBookmarkRequest request = new ReadAllBookmarkRequest(null);

        BookmarkCollectionResponse actual = bookmarkService.readBookmarks(1L, request, PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(actual.bookmarks()).hasSize(1),
                () -> assertThat(actual.lastBookmarkId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).bookmarkId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).accountId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).wordId()).isEqualTo(word.getId())
        );
    }
}
