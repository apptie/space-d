package com.dnd.spaced.core.bookmark.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookmarkTest {

    @Test
    void 북마크를_초기화한다() {
        // when & then
        Bookmark actual = assertDoesNotThrow(() -> new Bookmark(1L, 1L));

        assertAll(
                () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.getWordId()).isEqualTo(1L)
        );
    }

    @Test
    void 회원_식별자로_북마크의_생성자인지_확인한다() {
        // given
        Bookmark bookmark = new Bookmark(1L, 1L);

        // when
        boolean actual = bookmark.isCreator(1L);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 회원_식별자로_북마크의_생성자가_아닌지_확인한다() {
        // given
        Bookmark bookmark = new Bookmark(1L, 1L);

        // when
        boolean actual = bookmark.isNotCreator(1L);

        // then
        assertThat(actual).isFalse();
    }
}
