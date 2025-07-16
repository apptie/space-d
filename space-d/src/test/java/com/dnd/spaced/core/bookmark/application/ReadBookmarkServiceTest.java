package com.dnd.spaced.core.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReadBookmarkServiceTest {

    private static final long WORD_ID = 1L;
    private static final long ACCOUNT_ID = 1L;
    private static final long BOOKMARK_ID = 1L;
    private static final long LAST_BOOKMARK_ID = 1L;

    @Autowired
    ReadBookmarkService readBookmarkService;

    @Test
    @Sql(value = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 회원이_생성한_북마크를_모두_조회한다() {
        // given
        ReadAllBookmarkRequest request = new ReadAllBookmarkRequest(null);

        // when
        BookmarkCollectionResponse actual = readBookmarkService.readBookmarks(ACCOUNT_ID, request, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(actual.bookmarks()).hasSize(1),
                () -> assertThat(actual.lastBookmarkId()).isEqualTo(LAST_BOOKMARK_ID),
                () -> assertThat(actual.bookmarks().get(0).bookmarkId()).isEqualTo(BOOKMARK_ID),
                () -> assertThat(actual.bookmarks().get(0).accountId()).isEqualTo(ACCOUNT_ID),
                () -> assertThat(actual.bookmarks().get(0).wordId()).isEqualTo(WORD_ID)
        );
    }
}
