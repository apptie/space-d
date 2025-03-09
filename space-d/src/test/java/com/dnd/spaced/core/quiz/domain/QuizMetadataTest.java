package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizMetadataTest {

    @Test
    void 퀴즈_메타데이터를_초기화한다() {
        // when & then
        QuizMetadata actual = assertDoesNotThrow(() -> new QuizMetadata());

        assertAll(
                () -> assertThat(actual.getTotalQuizQuestionCount()).isEqualTo(0L),
                () -> assertThat(actual.getTotalTodayQuizQuestionCount()).isEqualTo(0L)
        );
    }
}
