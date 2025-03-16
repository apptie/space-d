package com.dnd.spaced.core.admin.application.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.admin.application.enums.exception.WordMetadataCounterNotFoundException;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.WordMetadata;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordMetadataCounterTest {

    @Test
    void 카테고리가_디자인인_용어가_추가되면_용어_메타데이터에_반영한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        WordMetadataCounter.add(Category.DESIGN, wordMetadata);

        // then
        assertAll(
                () -> assertThat(wordMetadata.getTotalWordCount()).isOne(),
                () -> assertThat(wordMetadata.getDesignWordCount()).isOne(),
                () -> assertThat(wordMetadata.getDevelopWordCount()).isZero(),
                () -> assertThat(wordMetadata.getBusinessWordCount()).isZero()
        );
    }

    @Test
    void 카테고리가_개발인_용어가_추가되면_용어_메타데이터에_반영한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        WordMetadataCounter.add(Category.DEVELOP, wordMetadata);

        // then
        assertAll(
                () -> assertThat(wordMetadata.getTotalWordCount()).isOne(),
                () -> assertThat(wordMetadata.getDesignWordCount()).isZero(),
                () -> assertThat(wordMetadata.getDevelopWordCount()).isOne(),
                () -> assertThat(wordMetadata.getBusinessWordCount()).isZero()
        );
    }

    @Test
    void 카테고리가_비즈니스인_용어가_추가되면_용어_메타데이터에_반영한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when
        WordMetadataCounter.add(Category.BUSINESS, wordMetadata);

        // then
        assertAll(
                () -> assertThat(wordMetadata.getTotalWordCount()).isOne(),
                () -> assertThat(wordMetadata.getDesignWordCount()).isZero(),
                () -> assertThat(wordMetadata.getDevelopWordCount()).isZero(),
                () -> assertThat(wordMetadata.getBusinessWordCount()).isOne()
        );
    }

    @ParameterizedTest(name = "카테고리가 {0}이라면 메타데이터에 용어가 추가된 것을 반영할 수 없다")
    @NullSource
    void 유효한_카테고리가_아니라면_용어_메타데이터에_용어가_추가된_것을_반영할_수_없다(Category invalidCategory) {
        // given
        WordMetadata wordMetadata = new WordMetadata();

        // when & then
        assertThatThrownBy(() -> WordMetadataCounter.add(invalidCategory, wordMetadata))
                .isInstanceOf(WordMetadataCounterNotFoundException.class)
                .hasMessage("용어 메타데이터에 변경사항을 반영하지 못했습니다.");
    }
}
