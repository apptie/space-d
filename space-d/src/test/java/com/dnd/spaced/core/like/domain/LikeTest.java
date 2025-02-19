package com.dnd.spaced.core.like.domain;

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
        assertDoesNotThrow(() -> new Like("user1@naver.com", 1L));
    }
}
