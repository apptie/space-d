package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.exception.InvalidQuizOptionContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizOptionTest {

    @Test
    void 퀴즈_보기를_초기화한다() {
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
        QuizOption actual = assertDoesNotThrow(() -> QuizOption.of(1L, "Authorization", 0, quizQuestion));

        assertAll(
                () -> assertThat(actual.getWordId()).isEqualTo(1L),
                () -> assertThat(actual.getContent()).isEqualTo("Authorization"),
                () -> assertThat(actual.getIndex()).isEqualTo(0),
                () -> assertThat(actual.getQuizQuestion()).isEqualTo(quizQuestion)
        );
    }

    @ParameterizedTest(name = "퀴즈 보기 내용이 {0}일 때 퀴즈 보기를 초기화할 수 없다")
    @NullAndEmptySource
    void 유효한_길이의_퀴즈_보기_내용이_아니라면_퀴즈_보기를_초기화할_수_없다(String invalidContent) {
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
        assertThatThrownBy(() -> QuizOption.of(1L, invalidContent, 0, quizQuestion))
                .isInstanceOf(InvalidQuizOptionContentException.class)
                .hasMessage("유효한 길이의 퀴즈 답 보기가 아닙니다.");
    }
}
