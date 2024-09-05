package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.word.domain.exception.InvalidCategoryNameException;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CategoryTest {

    @ParameterizedTest
    @MethodSource("findByTestWithCategoryName")
    void findBy_메서드는_유효한_이름을_전달하면_이름에_맞는_Category를_반환한다(String name) {
        // when
        Category category = Category.findBy(name);

        // then
        assertThat(category.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findBy_메서드는_유효하지_않은_이름을_전달하면_InvalidCategoryNameException_예외가_발생한다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> Category.findBy(invalidName))
                .isInstanceOf(InvalidCategoryNameException.class)
                .hasMessageContaining("잘못된 카테고리 이름");
    }

    private static Stream<Arguments> findByTestWithCategoryName() {
        return Arrays.stream(Category.values())
                     .map(category -> Arguments.of(category.getName()));
    }
}
