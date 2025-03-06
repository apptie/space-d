package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordMetadataTest {

    @Test
    void 용어_메타데이터를_초기화한다() {
        // when & then
        WordMetadata actual = assertDoesNotThrow(WordMetadata::new);

        assertAll(
                () -> assertThat(actual.getTotalWordCount()).isZero(),
                () -> assertThat(actual.getBusinessWordCount()).isZero(),
                () -> assertThat(actual.getDesignWordCount()).isZero(),
                () -> assertThat(actual.getDevelopWordCount()).isZero()
        );
    }

    @Test
    void 비즈니스_용어_개수를_증가시킨다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        wordMetadata.addBusinessWordCount();

        // then
        assertAll(
                () -> assertThat(wordMetadata.getTotalWordCount()).isOne(),
                () -> assertThat(wordMetadata.getBusinessWordCount()).isOne(),
                () -> assertThat(wordMetadata.getDesignWordCount()).isZero(),
                () -> assertThat(wordMetadata.getDevelopWordCount()).isZero()
        );
    }

    @Test
    void 디자인_용어_개수를_증가시킨다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        wordMetadata.addDesignWordCount();

        // then
        assertAll(
                () -> assertThat(wordMetadata.getTotalWordCount()).isOne(),
                () -> assertThat(wordMetadata.getBusinessWordCount()).isZero(),
                () -> assertThat(wordMetadata.getDesignWordCount()).isOne(),
                () -> assertThat(wordMetadata.getDevelopWordCount()).isZero()
        );
    }

    @Test
    void 개발_용어_개수를_증가시킨다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        wordMetadata.addDevelopWordCount();

        // then
        assertAll(
                () -> assertThat(wordMetadata.getTotalWordCount()).isOne(),
                () -> assertThat(wordMetadata.getBusinessWordCount()).isZero(),
                () -> assertThat(wordMetadata.getDesignWordCount()).isZero(),
                () -> assertThat(wordMetadata.getDevelopWordCount()).isOne()
        );
    }

    @Test
    void 비즈니스_퀴즈를_생성할_수_있는_용어_개수를_가졌는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = wordMetadata.canGenerateBusinessQuiz(5);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 디자인_퀴즈를_생성할_수_있는_용어_개수를_가졌는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = wordMetadata.canGenerateDesignQuiz(5);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 개발_퀴즈를_생성할_수_있는_용어_개수를_가졌는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = wordMetadata.canGenerateDevelopQuiz(5);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 전체_실무_용어_퀴즈를_생성할_수_있는_용어_개수를_가졌는지_여부를_확인한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        boolean actual = wordMetadata.canGenerateTotalQuiz(5);

        // then
        assertThat(actual).isFalse();
    }
}
