package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.exception.InvalidQuizQuestionContentException;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizQuestionTest {

    @Test
    void 퀴즈_문제를_초기화한다() {
        // given
        Quiz quiz = new Quiz(1L);
        QuizAnswerOption quizAnswerOption = new QuizAnswerOption(1L, "Authorization");
        QuizCategory quizCategory = QuizCategory.findBy("개발");

        // when & then
        QuizQuestion actual = assertDoesNotThrow(
                () -> QuizQuestion.of(
                        quizCategory,
                        "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                        "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                        quizAnswerOption,
                        quiz
                )
        );

        assertAll(
                () -> assertThat(actual.getQuizCategory()).isEqualTo(quizCategory),
                () -> assertThat(actual.getQuestionContent()).isEqualTo("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                () -> assertThat(actual.getQuestionExample()).isEqualTo("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                () -> assertThat(actual.getQuizAnswerOption()).isEqualTo(quizAnswerOption),
                () -> assertThat(actual.getQuiz()).isEqualTo(quiz)
        );
    }

    @ParameterizedTest(name = "질문이 {0} 일 때 퀴즈 문제를 초기화할 수 없다")
    @NullAndEmptySource
    void 퀴즈_문제는_유효하지_않은_길이의_질문이_아니라면_퀴즈_문제를_초기화_할_수_없다(String invalidQuestion) {
        // given
        Quiz quiz = new Quiz(1L);
        QuizAnswerOption quizAnswerOption = new QuizAnswerOption(1L, "Authorization");
        QuizCategory quizCategory = QuizCategory.findBy("개발");

        // when & then
        assertThatThrownBy(
                () -> QuizQuestion.of(
                        quizCategory,
                        invalidQuestion,
                        "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                        quizAnswerOption,
                        quiz
                )
        );
    }

    @ParameterizedTest(name = "질문이 {0} 일 때 퀴즈 문제를 초기화할 수 없다")
    @NullAndEmptySource
    void 퀴즈_문제는_유효하지_않은_길이의_예문이_아니라면_퀴즈_문제를_초기화_할_수_없다(String invalidQuestionContent) {
        // given
        Quiz quiz = new Quiz(1L);
        QuizAnswerOption quizAnswerOption = new QuizAnswerOption(1L, "Authorization");
        QuizCategory quizCategory = QuizCategory.findBy("개발");

        // when & then
        assertThatThrownBy(
                () -> QuizQuestion.of(
                        quizCategory,
                        "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                        invalidQuestionContent,
                        quizAnswerOption,
                        quiz
                )
        ).isInstanceOf(InvalidQuizQuestionContentException.class)
         .hasMessage("유효한 길이의 퀴즈 문제 지문이 아닙니다.");
    }

    @Test
    void 제출한_퀴즈_보기가_정답인지_여부를_확인한다() {
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
        boolean actual = quizQuestion.isCorrect(0);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 퀴즈_문제의_보기를_조회하면_외부에서_요소를_변경할_수_없다() {
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
        List<QuizOption> actual = quizQuestion.getQuizOptions();

        // then
        assertThatThrownBy(() -> actual.remove(0)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 선택한_보기_인덱스가_유효한_보기_인덱스인지_확인한다() {
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
        boolean actual = quizQuestion.isValidOptionIndex(0);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 선택한_보기_인덱스가_유효하지_않은_보기_인덱스인지_확인한다() {
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
        boolean actual = quizQuestion.isInvalidOptionIndex(5);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 퀴즈_문제에_퀴즈_보기를_동기화한다() {
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
        QuizOption quizOption = QuizOption.of(1L, "Authorization", 0, quizQuestion.getId());

        // when
        quizQuestion.initQuizOption(quizOption);

        // then
        assertAll(
                () -> assertThat(quizQuestion.getQuizOptions()).hasSize(2),
                () -> assertThat(quizQuestion.getQuizOptions()).containsExactly(quizOption, quizOption)
        );
    }
}
