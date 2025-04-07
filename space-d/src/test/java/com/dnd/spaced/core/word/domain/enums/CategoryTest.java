package com.dnd.spaced.core.word.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CategoryTest {

    private static Stream<Arguments> findByTestWithCategoryName() {
        return Arrays.stream(Category.values())
                     .map(category -> Arguments.of(category.getName()));
    }

    @ParameterizedTest
    @MethodSource("findByTestWithCategoryName")
    void 카테고리를_이름으로_조회한다(String name) {
        // when
        Optional<Category> actual = Category.findBy(name);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getName()).isEqualTo(name)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 카테고리에_없는_이름으로_조회하면_카테고리를_조회할_수_없다(String invalidName) {
        // when
        Optional<Category> actual = Category.findBy(invalidName);

        // then
        assertThat(actual).isNotPresent();
    }

    @Test
    void 해당_카테고리가_개발_카테고리인지_확인한다() {
        // when
        boolean actual = Category.DEVELOP.isDevelop();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 해당_카테고리가_디자인_카테고리인지_확인한다() {
        // when
        boolean actual = Category.DESIGN.isDesign();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 해당_카테고리가_비즈니스_카테고리인지_확인한다() {
        // when
        boolean actual = Category.BUSINESS.isBusiness();

        // then
        assertThat(actual).isTrue();
    }
}
