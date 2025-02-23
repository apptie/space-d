package com.dnd.spaced.core.like.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LikeTest {

    @Test
    void 좋아요를_초기화한다() {
        // when & then
        Like actual = assertDoesNotThrow(() -> new Like(1L, 1L));

        assertAll(
                () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.getCommentId()).isEqualTo(1L)
        );
    }
}
