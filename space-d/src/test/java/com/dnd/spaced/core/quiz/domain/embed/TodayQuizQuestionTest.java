package com.dnd.spaced.core.quiz.domain.embed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.embed.exception.InvalidTodayQuizExampleContentException;
import com.dnd.spaced.core.quiz.domain.embed.exception.InvalidTodayQuizQuestionException;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TodayQuizQuestionTest {

    @Test
    void 오늘의_퀴즈_문제를_초기화한다() {
        // given
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(1L, "Authorization");

        // when & then
        TodayQuizQuestion actual = assertDoesNotThrow(
                () -> TodayQuizQuestion.of(
                        QuizCategory.DEVELOP,
                        "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                        "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                        todayQuizAnswerOption
                )
        );

        assertAll(
                () -> assertThat(actual.getQuizCategory()).isEqualTo(QuizCategory.DEVELOP),
                () -> assertThat(actual.getQuestion()).isEqualTo("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                () -> assertThat(actual.getPassage()).isEqualTo("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
        );
    }

    @ParameterizedTest(name = "오늘의 퀴즈 문제 내용이 {0}이라면 오늘의 퀴즈 문제를 초기화할 수 없다")
    @NullAndEmptySource
    void 유효하지_않은_길이의_오늘의_퀴즈_문제_내용이라면_오늘의_퀴즈_문제를_초기화할_수_없다(String invalidQuestionContent) {
        // given
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(1L, "Authorization");

        // when & then
        assertThatThrownBy(
                () -> TodayQuizQuestion.of(
                        QuizCategory.DEVELOP,
                        invalidQuestionContent,
                        "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                        todayQuizAnswerOption
                )
        ).isInstanceOf(InvalidTodayQuizQuestionException.class)
         .hasMessage("유효한 길이의 퀴즈 질문이 아닙니다.");
    }

    @ParameterizedTest(name = "오늘의 퀴즈 문제 예문이 {0}이라면 오늘의 퀴즈 문제를 초기화할 수 없다")
    @NullAndEmptySource
    void 유효하지_않은_길이의_오늘의_퀴즈_문제_예문이라면_오늘의_퀴즈_문제를_초기화할_수_없다(String invalidQuestionExample) {
        // given
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(1L, "Authorization");

        // when & then
        assertThatThrownBy(
                () -> TodayQuizQuestion.of(
                        QuizCategory.DEVELOP,
                        "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                        invalidQuestionExample,
                        todayQuizAnswerOption
                )
        ).isInstanceOf(InvalidTodayQuizExampleContentException.class)
         .hasMessage("유효한 길이의 퀴즈 지문이 아닙니다.");
    }

    @Test
    void 사용자가_선택한_보기가_답인지_확인한다() {
        // given
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(1L, "Authorization");
        TodayQuizQuestion todayQuizQuestion = TodayQuizQuestion.of(
                QuizCategory.DEVELOP,
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                todayQuizAnswerOption
        );
        TodayQuiz todayQuiz = new TodayQuiz(todayQuizQuestion);
        TodayQuizOption.of(1L, "Authorization", 0, todayQuiz);
        TodayQuizOption.of(2L, "Domain", 1, todayQuiz);
        TodayQuizOption.of(3L, "Controller", 2, todayQuiz);
        TodayQuizOption.of(4L, "ViewResolver", 3, todayQuiz);

        // when
        boolean actual = todayQuizQuestion.isCorrect(1L);

        // then
        assertThat(actual).isTrue();
    }
}
