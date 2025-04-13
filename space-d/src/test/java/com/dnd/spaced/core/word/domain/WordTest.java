package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.domain.exception.InvalidCategoryNameException;
import com.dnd.spaced.core.word.domain.embed.exception.InvalidWordMeaningException;
import com.dnd.spaced.core.word.domain.exception.InvalidWordNameException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordTest {

    @Test
    void 용어를_초기화한다() {
        // when
        String name = "Authorization";
        String categoryName = "개발";
        String meaning = "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘";
        Word word = Word.builder()
                        .name(name)
                        .categoryName(categoryName)
                        .meaning(meaning)
                        .build();

        // then
        assertAll(
                () -> assertThat(word.getName()).isEqualTo(name),
                () -> assertThat(word.getWordMeaning().getMeaning()).isEqualTo(meaning),
                () -> assertThat(word.getCategory().getName()).isEqualTo(categoryName)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비어_있는_용어_이름이라면_용어를_초기화_할_수_없다(String invalidName) {
        // when & then
        assertThatThrownBy(
                () -> Word.builder()
                          .name(invalidName)
                          .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                          .categoryName("개발")
                          .build()
        ).isInstanceOf(InvalidWordNameException.class)
         .hasMessage("용어 이름은 null이거나 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비어_있는_용어_뜻이라면_용어를_초기화_할_수_없다(String invalidMeaning) {
        // when & then
        assertThatThrownBy(
                () -> Word.builder()
                          .name("Authorization")
                          .meaning(invalidMeaning)
                          .categoryName("개발")
                          .build()
        ).isInstanceOf(InvalidWordMeaningException.class)
         .hasMessage("용어 뜻은 최소 10글자 이상, 최대 150글자 이하여야 합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비어_있는_카테고리_이름을_전달하면_용어를_초기화_할_수_없다(String invalidCategoryName) {
        // when & then
        assertThatThrownBy(
                () -> Word.builder()
                          .name("Authorization")
                          .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                          .categoryName(invalidCategoryName)
                          .build()
        ).isInstanceOf(InvalidCategoryNameException.class)
         .hasMessageContaining("잘못된 카테고리 이름");
    }

    @Test
    void 용어에_용어_발음_정보를_추가한다() {
        // given
        Pronunciation pronunciation = new Pronunciation("어써라이제이션", "한글 발음");
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        // when
        word.addPronunciation(pronunciation);

        // then
        assertThat(word.getPronunciations()).hasSize(1);
    }

    @Test
    void 용어에_용어_예문을_추가한다() {
        // given
        WordExample wordExample = WordExample.from("시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다.");
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        // when
        word.addWordExample(wordExample);

        // then
        assertThat(word.getWordExamples()).hasSize(1);
    }

    @Test
    void 용어_조회수를_증가시킨다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        // when
        word.addViewCount();

        // then
        assertThat(word.getViewCount()).isEqualTo(1L);
    }

    @Test
    void 용어_뜻을_변경한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        // when
        String changedMeaning = "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘";

        word.changeMeaning(changedMeaning);

        // then
        assertThat(word.getWordMeaning().getMeaning()).isEqualTo(changedMeaning);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 길이가_유효하지_않은_용어_뜻이라면_용어를_초기화_할_수_없다(String invalidMeaning) {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        // when & then
        assertThatThrownBy(() -> word.changeMeaning(invalidMeaning))
                .isInstanceOf(InvalidWordMeaningException.class)
                .hasMessage("용어 뜻은 최소 10글자 이상, 최대 150글자 이하여야 합니다.");
    }
}
