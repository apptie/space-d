package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.exception.InvalidTodayQuizOptionContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TodayQuizOptionTest {

    @Test
    void 오늘의_퀴즈_보기를_초기화한다() {
        // given
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(1L, "Authorization");
        TodayQuizQuestion todayQuizQuestion = TodayQuizQuestion.of(
                QuizCategory.DEVELOP,
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                todayQuizAnswerOption
        );
        TodayQuiz todayQuiz = new TodayQuiz(todayQuizQuestion);

        // when & then
        TodayQuizOption actual = assertDoesNotThrow(
                () -> TodayQuizOption.of(
                        1L ,
                        "Authorization",
                        1,
                        todayQuiz
                )
        );

        assertThat(actual.getContent()).isEqualTo("Authorization");
    }

    @ParameterizedTest(name = "퀴즈 보기 내용이 {0}일 때 퀴즈 보기를 초기화할 수 없다")
    @NullAndEmptySource
    void 유효한_길이의_퀴즈_보기_내용이_아니라면_오늘의_퀴즈_보기를_초기화할_수_없다(String invalidContent) {
        // given
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(1L, "Authorization");
        TodayQuizQuestion todayQuizQuestion = TodayQuizQuestion.of(
                QuizCategory.DEVELOP,
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                todayQuizAnswerOption
        );
        TodayQuiz todayQuiz = new TodayQuiz(todayQuizQuestion);

        // when & then
        assertThatThrownBy(
                () -> TodayQuizOption.of(
                        1L ,
                        invalidContent,
                        1,
                        todayQuiz
                )
        ).isInstanceOf(InvalidTodayQuizOptionContentException.class)
         .hasMessage("유효한 길이의 퀴즈 답 보기가 아닙니다.");
    }
}
