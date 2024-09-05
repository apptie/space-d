package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.word.domain.exception.InvalidPronunciationContentException;
import com.dnd.spaced.core.word.domain.exception.InvalidPronunciationTypeNameException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PronunciationTest {

    @Test
    void 생성자는_유효한_content와_typeName을_전달하면_Pronunciation을_초기화하고_반환한다() {
        // when & then
        assertDoesNotThrow(() -> new Pronunciation("어써라이제이션", "한글 발음"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성자는_유효하지_않은_content를_전달하면_InvalidPronunciationContentException_예외가_발생한다(String invalidContent) {
        // when & then
        assertThatThrownBy(() -> new Pronunciation(invalidContent, "한글 발음"))
                .isInstanceOf(InvalidPronunciationContentException.class)
                .hasMessage("발음은 null이거나 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성자는_유효하지_않은_typeName을_전달하면_InvalidPronunciationContentException_예외가_발생한다(String invalidTypeName) {
        // when & then
        assertThatThrownBy(() -> new Pronunciation("어써라이제이션", invalidTypeName))
                .isInstanceOf(InvalidPronunciationTypeNameException.class)
                .hasMessageContaining("잘못된 발음 타입");
    }

    @Test
    void initWord_메서드는_word를_전달하면_전달한_word로_초기화한다() {
        // given
        Pronunciation pronunciation = new Pronunciation("어써라이제이션", "한글 발음");
        Word word = Word.builder()
                         .name("Authorization")
                         .meaning("word meaning")
                         .categoryName("개발")
                         .build();

        // when
        pronunciation.initWord(word);

        // then
        assertThat(pronunciation.getWord()).isNotNull();
    }
}
