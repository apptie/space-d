package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.word.domain.exception.InvalidPronunciationTypeNameException;
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
class PronunciationTypeTest {

    @ParameterizedTest
    @MethodSource("findByTestWithPronunciationTypeName")
    void findBy_메서드는_유효한_이름을_전달하면_이름에_맞는_PronunciationType을_반환한다(String name) {
        // when
        PronunciationType pronunciationType = PronunciationType.findBy(name);

        // then
        assertThat(pronunciationType.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findBy_메서드는_유효하지_않은_이름을_전달하면_InvalidPronunciationTypeNameException_예외가_발생한다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> PronunciationType.findBy(invalidName))
                .isInstanceOf(InvalidPronunciationTypeNameException.class)
                .hasMessageContaining("잘못된 발음 타입");
    }

    private static Stream<Arguments> findByTestWithPronunciationTypeName() {
        return Arrays.stream(PronunciationType.values())
                     .map(pronunciationType -> Arguments.of(pronunciationType.getName()));
    }
}
