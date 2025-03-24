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
}
