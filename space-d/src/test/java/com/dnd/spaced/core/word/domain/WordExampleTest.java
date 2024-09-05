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
    void 생성자는_유효한_example을_전달하면_WordExample을_초기화하고_반환한다() {
        // when
        String example = "example";
        WordExample wordExample = new WordExample(example);

        // then
        assertThat(wordExample.getExample()).isEqualTo(example);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성자는_유효하지_않은_example을_전달하면_InvalidWordExampleContentException_예외가_발생한다(String invalidExample) {
        // when & then
        assertThatThrownBy(() -> new WordExample(invalidExample))
                .isInstanceOf(InvalidWordExampleContentException.class)
                .hasMessage("예문의 길이는 최소 1글자 이상, 최대 50글자 이하여야 합니다.");
    }

    @Test
    void initWord_메서드는_word를_전달하면_전달한_word로_초기화한다() {
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
    void updateContent_메서드는_유효한_example을_전달하면_해당_example로_변경한다() {
        // given
        WordExample wordExample = new WordExample("example");

        // when
        String changedExample = "changedExample";
        wordExample.changeExample(changedExample);
    }

    @Test
    void isEqualTo_메서드는_전달한_id와_일치하는지_여부를_반환한다() {
        // given
        WordExample wordExample = new WordExample("example");
        ReflectionTestUtils.setField(wordExample, "id", 1L);

        // when
        boolean actual = wordExample.isEqualTo(1L);

        // then
        assertThat(actual).isTrue();
    }
}
