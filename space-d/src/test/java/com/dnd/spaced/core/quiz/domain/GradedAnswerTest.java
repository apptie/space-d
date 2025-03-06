package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.exception.InvalidSubmittedQuizOptionIndexException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GradedAnswerTest {

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
        QuizOption.of(1L, "Authorization", 0, quizQuestion);
        QuizOption.of(2L, "Domain", 1, quizQuestion);
        QuizOption.of(3L, "Controller", 2, quizQuestion);
        QuizOption.of(4L, "HashMap", 3, quizQuestion);

        // when & then
        GradedAnswer actual = assertDoesNotThrow(
                () -> GradedAnswer.of(1L, 1L, quizQuestion, 3)
        );

        assertAll(
                () -> assertThat(actual.getQuizId()).isEqualTo(1L),
                () -> assertThat(actual.getQuizQuestion()).isEqualTo(quizQuestion),
                () -> assertThat(actual.getSelectedOptionIndex()).isEqualTo(3)
        );
    }

    @Test
    void 퀴즈_문제에서_없는_보기를_선택한_경우_퀴즈_채점_결과를_초기화_할_수_없다() {
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
        QuizOption.of(1L, "Authorization", 0, quizQuestion);
        QuizOption.of(2L, "Domain", 1, quizQuestion);
        QuizOption.of(3L, "Controller", 2, quizQuestion);
        QuizOption.of(4L, "HashMap", 3, quizQuestion);

        // when & then
        assertThatThrownBy(
                () -> GradedAnswer.of(1L, 1L, quizQuestion, -1)
        ).isInstanceOf(InvalidSubmittedQuizOptionIndexException.class)
         .hasMessage("없는 보기를 선택했습니다.");
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
        QuizOption.of(1L, "Authorization", 0, quizQuestion);
        QuizOption.of(2L, "Domain", 1, quizQuestion);
        QuizOption.of(3L, "Controller", 2, quizQuestion);
        QuizOption.of(4L, "HashMap", 3, quizQuestion);
        GradedAnswer gradedAnswer = GradedAnswer.of(1L, 1L, quizQuestion, 3);

        // when
        boolean actual = gradedAnswer.isCorrect();

        // then
        assertThat(actual).isFalse();
    }
}
