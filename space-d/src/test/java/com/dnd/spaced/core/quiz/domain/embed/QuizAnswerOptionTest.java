package com.dnd.spaced.core.quiz.domain.embed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizAnswerOptionTest {

    @Test
    void 퀴즈_정답_보기를_초기화한다() {
        // when & then
        QuizAnswerOption actual = assertDoesNotThrow(() -> new QuizAnswerOption(1L, "Authorization"));

        assertAll(
                () -> assertThat(actual.getWordId()).isEqualTo(1L),
                () -> assertThat(actual.getContent()).isEqualTo("Authorization")
        );
    }

    @Test
    void 퀴즈_정답_보기의_용어_ID가_일치하는지_여부를_확인한다() {
        // given
        QuizAnswerOption quizAnswerOption = new QuizAnswerOption(1L, "Authorization");

        // when
        boolean actual = quizAnswerOption.matchesWordId(1L);

        // then
        assertThat(actual).isTrue();
    }
}
