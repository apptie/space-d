package com.dnd.spaced.core.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.core.bookmark.application.dto.request.DeleteBookmarkRequest;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
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
class DeleteBookmarkServiceTest {

    private static final Long WORD_ID = 1L;
    private static final Long ACCOUNT_ID = 1L;

    @Autowired
    DeleteBookmarkService deleteBookmarkService;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Test
    @Sql(scripts = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 북마크를_삭제한다() {
        // given
        assertThat(bookmarkRepository.existsBy(ACCOUNT_ID, WORD_ID)).isTrue();

        DeleteBookmarkRequest request = new DeleteBookmarkRequest(WORD_ID);

        // when
        deleteBookmarkService.deleteBookmark(ACCOUNT_ID, request);

        // then
        assertThat(bookmarkRepository.existsBy(ACCOUNT_ID, WORD_ID)).isFalse();
    }

}
