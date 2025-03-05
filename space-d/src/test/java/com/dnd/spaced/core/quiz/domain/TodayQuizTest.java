package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.exception.InvalidSubmittedTodayQuizOptionIndexException;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TodayQuizTest {

    @Test
    void 오늘의_퀴즈를_초기화한다() {
        // given
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(1L, "Authorization");
        TodayQuizQuestion todayQuizQuestion = TodayQuizQuestion.of(
                QuizCategory.DEVELOP,
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                todayQuizAnswerOption
        );

        // when & then
        TodayQuiz actual = assertDoesNotThrow(() -> new TodayQuiz(todayQuizQuestion));

        assertThat(actual.getTodayQuizQuestion()).isEqualTo(todayQuizQuestion);
    }

    @Test
    void 사용자가_제출한_보기_순서가_오늘의_퀴즈_정답인지_확인한다() {
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
        boolean actual = todayQuiz.isCorrect(0);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 사용자가_제출한_보기_순서가_오늘의_퀴즈_보기에_없다면_정답인지_확인할_수_없다() {
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
        assertThatThrownBy(() -> todayQuiz.isCorrect(-1))
                .isInstanceOf(InvalidSubmittedTodayQuizOptionIndexException.class)
                .hasMessage("없는 보기를 선택했습니다.");
    }

    @Test
    void 사용자가_제출한_보기_순서로_오늘의_퀴즈를_채점한다() {
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
        TodayQuizGradedAnswer actual = todayQuiz.grade(1L, 0);

        // then
        assertAll(
                () -> assertThat(actual.getTodayQuiz()).isEqualTo(todayQuiz),
                () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.getSelectedOptionIndex()).isZero()
        );
    }

    @Test
    void 사용자가_제출한_보기_순서가_오늘의_퀴즈_보기에_없다면_오늘의_퀴즈_채점을_할_수_없다() {
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
        assertThatThrownBy(() -> todayQuiz.grade(1L, -1))
                .isInstanceOf(InvalidSubmittedTodayQuizOptionIndexException.class)
                .hasMessage("없는 보기를 선택했습니다.");
    }

    @Test
    void 오늘의_퀴즈_보기를_조회하면_외부에서_요소를_변경할_수_없다() {
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
        List<TodayQuizOption> actual = todayQuiz.getTodayQuizOptions();

        // then
        assertThatThrownBy(() -> actual.remove(0)).isInstanceOf(UnsupportedOperationException.class);
    }
}
