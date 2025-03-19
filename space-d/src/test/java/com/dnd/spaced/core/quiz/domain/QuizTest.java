package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.exception.InvalidSubmittedAnswersCountException;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizTest {

    @Test
    void 퀴즈를_초기화한다() {
        // when & then
        Quiz actual = assertDoesNotThrow(() -> new Quiz(1L));

        assertThat(actual.getAccountId()).isEqualTo(1L);
    }

    @Test
    void 회원이_제출한_답을_채점한다() {
        // given
        Quiz quiz = new Quiz(1L);
        ReflectionTestUtils.setField(quiz, "id", 6L);
        QuizAnswerOption quizAnswerOption = new QuizAnswerOption(1L, "Authorization");
        QuizCategory quizCategory = QuizCategory.findBy("개발");
        QuizQuestion quizQuestion = QuizQuestion.of(
                quizCategory,
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                quizAnswerOption,
                quiz
        );
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);

        // when
        List<GradedAnswer> actual = quiz.grade(1L, new int[]{0, 1, 2, 3, 2});

        // then
        assertAll(
                () -> assertThat(actual).hasSize(5),
                () -> assertThat(actual.get(0).getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.get(0).getQuizId()).isEqualTo(6L),
                () -> assertThat(actual.get(0).getSelectedOptionIndex()).isEqualTo(0),
                () -> assertThat(actual.get(1).getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.get(1).getQuizId()).isEqualTo(6L),
                () -> assertThat(actual.get(1).getSelectedOptionIndex()).isEqualTo(1),
                () -> assertThat(actual.get(2).getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.get(2).getQuizId()).isEqualTo(6L),
                () -> assertThat(actual.get(2).getSelectedOptionIndex()).isEqualTo(2),
                () -> assertThat(actual.get(3).getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.get(3).getQuizId()).isEqualTo(6L),
                () -> assertThat(actual.get(3).getSelectedOptionIndex()).isEqualTo(3),
                () -> assertThat(actual.get(4).getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.get(4).getQuizId()).isEqualTo(6L),
                () -> assertThat(actual.get(4).getSelectedOptionIndex()).isEqualTo(2)
        );
    }

    @Test
    void 문제_개수만큼_정답_개수를_입력하지_않으면_예외가_발생한다() {
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
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);

        // when & then
        assertThatThrownBy(() -> quiz.grade( 1L, new int[]{}))
                .isInstanceOf(InvalidSubmittedAnswersCountException.class)
                .hasMessage("문제 개수와 제출한 정답 개수가 다릅니다.");
    }

    @Test
    void 퀴즈_문제를_조회하면_외부에서_요소를_변경할_수_없다() {
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
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        quiz.initQuestion(quizQuestion);
        List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();

        // when & then
        assertThatThrownBy(() -> quizQuestions.remove(0)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 퀴즈의_퀴즈_문제를_동기화한다() {
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

        // when
        quiz.initQuestion(quizQuestion);

        // then
        assertThat(quiz.getQuizQuestions()).contains(quizQuestion);
    }
}
