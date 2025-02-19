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
    void 용어_뜻을_초기화한다() {
        // when
        String meaning = "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘";
        WordMeaning wordMeaning = new WordMeaning(meaning);

        // then
        assertThat(wordMeaning.getMeaning()).isEqualTo(meaning);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 글자_수가_유효하지_않은_단어_뜻이라면_용어_뜻을_초기화_할_수_없다(String invalidMeaning) {
        // when & then
        assertThatThrownBy(() -> new WordMeaning(invalidMeaning))
                .isInstanceOf(InvalidWordMeaningException.class)
                .hasMessage("용어 뜻은 최소 10글자 이상, 최대 150글자 이하여야 합니다.");
    }

    @Test
    void 용어_뜻을_변경한다() {
        // given
        WordMeaning wordMeaning = new WordMeaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘");

        // when
        String changedMeaning = "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘";
        wordMeaning.changeMeaning(changedMeaning);

        // then
        assertThat(wordMeaning.getMeaning()).isEqualTo(changedMeaning);
    }

    @ParameterizedTest(name = "변경하려는 용어 뜻이 {0}일 때 용어 뜻을 변경할 수 없다")
    @NullAndEmptySource
    void 글자_수가_유효하지_않은_단어_뜻이라면_용어_뜻을_변경할_수_없다(String invalidMeaning) {
        // given
        WordMeaning wordMeaning = new WordMeaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘");

        // when & then
        assertThatThrownBy(() -> wordMeaning.changeMeaning(invalidMeaning))
                .isInstanceOf(InvalidWordMeaningException.class)
                .hasMessage("용어 뜻은 최소 10글자 이상, 최대 150글자 이하여야 합니다.");
    }
}
