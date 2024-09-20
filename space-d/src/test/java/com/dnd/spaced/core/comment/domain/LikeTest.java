package com.dnd.spaced.core.comment.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.like.domain.Like;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LikeTest {

    @Test
    void 생성자는_accountId와_commentId를_전달하면_Like를_초기화하고_반환한다() {
        // when & then
        assertDoesNotThrow(() -> new Like("accountId", 1L));
    }
}
