package com.dnd.spaced.core.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.exception.AlreadyExistsBookmarkException;
import com.dnd.spaced.core.bookmark.application.exception.WordNotFoundException;
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
class CreateBookmarkServiceTest {

    private static final Long WORD_ID = 1L;
    private static final Long NOT_FOUND_WORD_ID = -999L;
    private static final Long ACCOUNT_ID = 1L;

    @Autowired
    CreateBookmarkService createBookmarkService;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Test
    @Sql("classpath:sql/bookmark/word.sql")
    void 북마크를_추가한다() {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(WORD_ID);

        // when
        createBookmarkService.createBookmark(ACCOUNT_ID, request);

        // then
        boolean actual = bookmarkRepository.existsBy(ACCOUNT_ID, WORD_ID);

        assertThat(actual).isTrue();
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 이미_북마크에_추가된_용어를_북마에_추가할_수_없다() {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(WORD_ID);

        // when & then
        assertThatThrownBy(() -> createBookmarkService.createBookmark(ACCOUNT_ID, request))
                .isInstanceOf(AlreadyExistsBookmarkException.class)
                .hasMessage("이미 북마크에 추가된 용어입니다.");
    }

    @Test
    void 지정한_용어_식별자로_용어를_찾지_못하면_북마크를_추가할_수_없다() {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(NOT_FOUND_WORD_ID);

        // when & then
        assertThatThrownBy(() -> createBookmarkService.createBookmark(ACCOUNT_ID, request))
                .isInstanceOf(WordNotFoundException.class)
                .hasMessage("지정한 식별자의 용어를 찾지 못했습니다.");

    }
}
