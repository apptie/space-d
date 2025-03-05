package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TodayQuizGradedAnswerTest {

    @Test
    void 오늘의_퀴즈_채점_결과를_초기화한다() {
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

        // when & then
        TodayQuizGradedAnswer actual = assertDoesNotThrow(
                () -> new TodayQuizGradedAnswer(1L, todayQuiz, 0)
        );

        assertAll(
                () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.getTodayQuiz()).isEqualTo(todayQuiz),
                () -> assertThat(actual.getSelectedOptionIndex()).isZero()
        );
    }

    @Test
    void 오늘의_퀴즈_채겸_결과를_조회한다() {
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
        TodayQuizGradedAnswer todayQuizGradedAnswer = new TodayQuizGradedAnswer(1L, todayQuiz, 0);

        // when
        boolean actual = todayQuizGradedAnswer.isCorrect();

        // then
        assertThat(actual).isTrue();
    }
}
