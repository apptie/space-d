package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.word.domain.exception.InvalidWordMeaningException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordMeaningTest {

    @Test
    void 단어_뜻을_초기화한다() {
        // when
        String meaning = "1234567890";
        WordMeaning wordMeaning = new WordMeaning(meaning);

        // then
        assertThat(wordMeaning.getMeaning()).isEqualTo(meaning);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 단어_뜻_초기화_시_유효하지_않은_단어_뜻이라면_예외가_발생한다(String invalidMeaning) {
        // when & then
        assertThatThrownBy(() -> new WordMeaning(invalidMeaning))
                .isInstanceOf(InvalidWordMeaningException.class)
                .hasMessage("용어 뜻은 최소 10글자 이상, 최대 150글자 이하여야 합니다.");
    }

    @Test
    void 단어_뜻을_변경한다() {
        // given
        WordMeaning wordMeaning = new WordMeaning("1234567890");

        // when
        String changedMeaning = "changedMeaning";
        wordMeaning.changeMeaning(changedMeaning);

        // then
        assertThat(wordMeaning.getMeaning()).isEqualTo(changedMeaning);
    }

    @ParameterizedTest(name = "용어 뜻이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 단어_뜻_변경_시_유효하지_않은_단어_뜻이라면_예외가_발생한다(String invalidMeaning) {
        // when & then
        assertThatThrownBy(() -> new WordMeaning(invalidMeaning))
                .isInstanceOf(InvalidWordMeaningException.class)
                .hasMessage("용어 뜻은 최소 10글자 이상, 최대 150글자 이하여야 합니다.");
    }
}
