package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordRandomTest {

    @Test
    void 용어_랜덤값을_초기화한다() {
        // when & then
        WordRandom actual = assertDoesNotThrow(() -> new WordRandom(1L, Category.BUSINESS, 82));

        assertAll(
                () -> assertThat(actual.getWordId()).isEqualTo(1L),
                () -> assertThat(actual.getCategory()).isEqualTo(Category.BUSINESS),
                () -> assertThat(actual.getRandom()).isEqualTo(82)
        );
    }
}
