package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.word.domain.exception.InvalidWordExampleContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordExampleTest {

    @Test
    void 단어_예문을_초기화한다() {
        // when
        String example = "example";
        WordExample wordExample = new WordExample(example);

        // then
        assertThat(wordExample.getExample()).isEqualTo(example);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유효하지_않은_단어_예문을_전달하면_예외가_발생한(String invalidExample) {
        // when & then
        assertThatThrownBy(() -> new WordExample(invalidExample))
                .isInstanceOf(InvalidWordExampleContentException.class)
                .hasMessage("예문의 길이는 최소 1글자 이상, 최대 150글자 이하여야 합니다.");
    }

    @Test
    void 단어_예문에_단어_정보를_추가한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();
        WordExample wordExample = new WordExample("example");

        // when
        wordExample.initWord(word);

        // then
        assertThat(wordExample.getWord()).isNotNull();
    }

    @Test
    void 단어_예문을_변경한다() {
        // given
        WordExample wordExample = new WordExample("example");
        String changedExample = "changedExample";

        // when
        wordExample.changeExample(changedExample);

        // then
        assertThat(wordExample.getExample()).isEqualTo(changedExample);
    }

    @Test
    void 단어_예문의_식별자_여부를_판단한다() {
        // given
        WordExample wordExample = new WordExample("example");
        ReflectionTestUtils.setField(wordExample, "id", 1L);

        // when
        boolean actual = wordExample.isEqualTo(1L);

        // then
        assertThat(actual).isTrue();
    }
}
