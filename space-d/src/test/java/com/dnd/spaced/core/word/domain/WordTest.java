package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.domain.exception.InvalidCategoryNameException;
import com.dnd.spaced.core.word.domain.exception.InvalidWordMeaningException;
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
    void 생성자는_유효한_name과_meaning을_전달하면_Word를_초기화하고_반환한다() {
        // when
        String name = "name";
        String meaning = "word meaning";
        String category = "개발";
        Word word = Word.builder()
                        .name(name)
                        .meaning(meaning)
                        .categoryName(category)
                        .build();

        // then
        assertAll(
                () -> assertThat(word.getName()).isEqualTo(name),
                () -> assertThat(word.getWordMeaning().getMeaning()).isEqualTo(meaning),
                () -> assertThat(word.getCategory().getName()).isEqualTo(category)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성자는_유효하지_않은_name을_전달하면_InvalidWordNameException_예외가_발생한다(String invalidName) {
        // when & then
        assertThatThrownBy(
                () -> Word.builder()
                          .name(invalidName)
                          .meaning("word meaning")
                          .categoryName("개발")
                          .build()
        ).isInstanceOf(InvalidWordNameException.class)
         .hasMessage("용어 이름은 null이거나 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성자는_유효하지_않은_meaning을_전달하면_InvalidWordMeaningException_예외가_발생한다(String invalidMeaning) {
        // when & then
        assertThatThrownBy(
                () -> Word.builder()
                          .name("name")
                          .meaning(invalidMeaning)
                          .categoryName("개발")
                          .build()
        ).isInstanceOf(InvalidWordMeaningException.class)
         .hasMessage("용어 뜻은 최소 10글자 이상, 최대 70글자 이하여야 합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성자는_유효하지_않은_categoryName을_전달하면_InvalidCategoryNameException_예외가_발생한다(String invalidCategoryName) {
        // when & then
        assertThatThrownBy(
                () -> Word.builder()
                          .name("name")
                          .meaning("word meaning")
                          .categoryName(invalidCategoryName)
                          .build()
        ).isInstanceOf(InvalidCategoryNameException.class)
         .hasMessageContaining("잘못된 카테고리 이름");
    }

    @Test
    void addPronunciation_메서드는_Pronunciation을_전달하면_Word에_Pronunciation을_추가한다() {
        // given
        Pronunciation pronunciation = new Pronunciation("어써라이제이션", "한글 발음");
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();

        // when
        word.addPronunciation(pronunciation);

        // then
        assertThat(word.getPronunciations()).hasSize(1);
    }

    @Test
    void addWordExample_메서드는_WordExample을_전달하면_Word에_WordExample을_추가한다() {
        // given
        WordExample wordExample = new WordExample("example");
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();

        // when
        word.addWordExample(wordExample);

        // then
        assertThat(word.getWordExamples()).hasSize(1);
    }

    @Test
    void addViewCount_메서드는_호출하면_viewCount를_1_증가시킨다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();

        // when
        word.addViewCount();

        // then
        assertThat(word.getViewCount()).isEqualTo(1L);
    }

    @Test
    void changeWordMeaning_메서드는_유효한_meaning을_전달하면_전달한_meaning으로_변경한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();

        // when
        String changedMeaning = "changed word meaning";

        word.changeMeaning(changedMeaning);

        // then
        assertThat(word.getWordMeaning().getMeaning()).isEqualTo(changedMeaning);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void changeWordMeaning_메서드는_유효하지_않은_meaning을_전달하면_InvalidWordMeaningException_예외가_발생한다(String invalidMeaning) {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();

        // when & then
        assertThatThrownBy(() -> word.changeMeaning(invalidMeaning))
                .isInstanceOf(InvalidWordMeaningException.class)
                .hasMessage("용어 뜻은 최소 10글자 이상, 최대 70글자 이하여야 합니다.");
    }
}
