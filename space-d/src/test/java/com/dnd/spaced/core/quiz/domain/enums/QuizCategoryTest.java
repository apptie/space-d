package com.dnd.spaced.core.quiz.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.quiz.domain.enums.exception.InvalidQuizCategoryNameException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizCategoryTest {

    private static Stream<Arguments> findByTestArguments() {
        return Stream.of(
                Arguments.of("비즈니스", QuizCategory.BUSINESS),
                Arguments.of("개발", QuizCategory.DEVELOP),
                Arguments.of("디자인", QuizCategory.DESIGN),
                Arguments.of("전체 실무", QuizCategory.TOTAL)
        );
    }

    @ParameterizedTest(name = "퀴즈 카테고리 이름이 {0}일 때 {1}을 반환한다")
    @MethodSource("findByTestArguments")
    void 퀴즈_카테고리_이름을_전달하면_이름에_맞는_퀴즈_카테고리를_반환한다(String name, QuizCategory expected) {
        // when
        QuizCategory actual = QuizCategory.findBy(name);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "퀴즈 카테고리 이름이 {0}일 때 퀴즈 카테고리를 조회할 수 없다")
    @NullAndEmptySource
    void 유효하지_않은_퀴즈_카테고리_이름을_전달하면_퀴즈_카테고리를_전달할_수_없다(String invalidQuizCategoryName) {
        // when & then
        assertThatThrownBy(() -> QuizCategory.findBy(invalidQuizCategoryName))
                .isInstanceOf(InvalidQuizCategoryNameException.class)
                .hasMessageContaining("잘못된 퀴즈 카테고리 이름");
    }

    @Test
    void 퀴즈_카테고리_중_하나를_랜덤으로_조회한다() {
        // when
        QuizCategory actual = QuizCategory.findRandom();

        // then
        assertThat(actual).isNotNull();
    }
}
