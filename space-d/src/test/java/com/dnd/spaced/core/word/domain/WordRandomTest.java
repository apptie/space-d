package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.word.domain.enums.Category;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordRandomTest {

    @Test
    void 용어_랜덤값을_초기화한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        // when & then
        WordRandom actual = assertDoesNotThrow(() -> new WordRandom(word, Category.BUSINESS, 82));

        assertAll(
                () -> assertThat(actual.getWord()).isEqualTo(word),
                () -> assertThat(actual.getCategory()).isEqualTo(Category.BUSINESS),
                () -> assertThat(actual.getRandom()).isEqualTo(82)
        );
    }
}
