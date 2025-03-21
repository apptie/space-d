package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizGradedAnswerTest {

    @Test
    void 퀴즈_채점_결과를_초기화한다() {
        // given
        Quiz quiz = new Quiz(1L);
        QuizAnswerOption quizAnswerOption = new QuizAnswerOption(1L, "Authorization");
        QuizCategory quizCategory = QuizCategory.findBy("개발");
        QuizQuestion quizQuestion = QuizQuestion.of(
                quizCategory,
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                quizAnswerOption,
                quiz
        );

        // when & then
        QuizGradedAnswer actual = assertDoesNotThrow(
                () -> QuizGradedAnswer.of(1L, 1L, quizQuestion, 1L, "Authorization")
        );

        assertAll(
                () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.getQuizId()).isEqualTo(1L),
                () -> assertThat(actual.getQuizQuestion()).isEqualTo(quizQuestion),
                () -> assertThat(actual.getSelectedWordId()).isEqualTo(1L),
                () -> assertThat(actual.getSelectedContent()).isEqualTo("Authorization")
        );
    }

    @Test
    void 해당_퀴즈의_정답_여부를_확인한다() {
        // given
        Quiz quiz = new Quiz(1L);
        QuizAnswerOption quizAnswerOption = new QuizAnswerOption(1L, "Authorization");
        QuizCategory quizCategory = QuizCategory.findBy("개발");
        QuizQuestion quizQuestion = QuizQuestion.of(
                quizCategory,
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                quizAnswerOption,
                quiz
        );
        QuizGradedAnswer quizGradedAnswer = QuizGradedAnswer.of(1L, 1L, quizQuestion, 1L, "Authorization");

        // when
        boolean actual = quizGradedAnswer.isCorrect();

        // then
        assertThat(actual).isTrue();
    }
}
