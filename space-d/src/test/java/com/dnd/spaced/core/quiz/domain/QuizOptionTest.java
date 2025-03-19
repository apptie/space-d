package com.dnd.spaced.core.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        // when & then
        QuizOption actual = assertDoesNotThrow(() -> QuizOption.of(1L, "Authorization", 2, 1L));

        assertAll(
                () -> assertThat(actual.getWordId()).isEqualTo(1L),
                () -> assertThat(actual.getContent()).isEqualTo("Authorization"),
                () -> assertThat(actual.getOptionOrder()).isEqualTo(2),
                () -> assertThat(actual.getQuizQuestionId()).isEqualTo(1L)
        );
    }

    @ParameterizedTest(name = "퀴즈 보기 내용이 {0}일 때 퀴즈 보기를 초기화할 수 없다")
    @NullAndEmptySource
    void 유효한_길이의_퀴즈_보기_내용이_아니라면_퀴즈_보기를_초기화할_수_없다(String invalidContent) {
        // when & then
        assertThatThrownBy(() -> QuizOption.of(1L, invalidContent, 0, 1L))
                .isInstanceOf(InvalidQuizOptionContentException.class)
                .hasMessage("유효한 길이의 퀴즈 답 보기가 아닙니다.");
    }
}
