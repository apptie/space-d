package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.word.domain.exception.InvalidPronunciationContentException;
import com.dnd.spaced.core.word.domain.enums.exception.InvalidPronunciationTypeNameException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PronunciationTest {

    @Test
    void 발음_정보를_초기화한다() {
        // when & then
        assertDoesNotThrow(() -> Pronunciation.of("어써라이제이션", "한글 발음"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 발음_내용이_비어_있다면_발음_정보를_초기화_할_수_없다(String invalidContent) {
        // when & then
        assertThatThrownBy(() -> Pronunciation.of(invalidContent, "한글 발음"))
                .isInstanceOf(InvalidPronunciationContentException.class)
                .hasMessage("발음은 null이거나 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 발음_유형이_유효하지_않다면_발음_정보를_초기화_할_수_없다(String invalidTypeName) {
        // when & then
        assertThatThrownBy(() -> Pronunciation.of("어써라이제이션", invalidTypeName))
                .isInstanceOf(InvalidPronunciationTypeNameException.class)
                .hasMessageContaining("잘못된 발음 타입");
    }

    @Test
    void 발음_정보에_단어_정보를_추가한다() {
        // given
        Pronunciation pronunciation = Pronunciation.of("어써라이제이션", "한글 발음");
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        // when
        pronunciation.initWord(word);

        // then
        assertThat(pronunciation.getWord()).isNotNull();
    }
}
