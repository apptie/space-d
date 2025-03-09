package com.dnd.spaced.core.quiz.application.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.application.enums.exception.QuizCategoryNotFoundException;
import com.dnd.spaced.core.word.domain.WordMetadata;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizWordCountValidatorTest {

    @Test
    void 디자인_퀴즈를_생성할_수_있는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = QuizWordCountValidator.isValidate(QuizCategory.DESIGN, wordMetadata, 5);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 디자인_퀴즈를_생성할_수_없는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = QuizWordCountValidator.isInvalidate(QuizCategory.DESIGN, wordMetadata, 5);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 비즈니스_퀴즈를_생성할_수_있는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = QuizWordCountValidator.isValidate(QuizCategory.BUSINESS, wordMetadata, 5);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 비즈니스_퀴즈를_생성할_수_없는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = QuizWordCountValidator.isInvalidate(QuizCategory.BUSINESS, wordMetadata, 5);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 개발_퀴즈를_생성할_수_있는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = QuizWordCountValidator.isValidate(QuizCategory.DEVELOP, wordMetadata, 5);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 개발_퀴즈를_생성할_수_없는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = QuizWordCountValidator.isInvalidate(QuizCategory.DEVELOP, wordMetadata, 5);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 전체_실무_퀴즈를_생성할_수_있는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = QuizWordCountValidator.isValidate(QuizCategory.TOTAL, wordMetadata, 5);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 전체_실무_퀴즈를_생성할_수_없는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = QuizWordCountValidator.isInvalidate(QuizCategory.TOTAL, wordMetadata, 5);

        // then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest(name = "카테고리가 {0} 일 때 퀴즈 생성 여부를 판단할 수 없다")
    @NullSource
    void 유효한_퀴즈_카테고리가_아니라면_퀴즈_생성_가능_여부를_판단할_수_없다(QuizCategory invalidQuizCategory) {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when & then
        assertThatThrownBy(
                () -> QuizWordCountValidator.isValidate(invalidQuizCategory, wordMetadata, 5)
        ).isInstanceOf(QuizCategoryNotFoundException.class)
         .hasMessage("지정한 퀴즈 카테고리를 찾을 수 없습니다.");
    }
}
