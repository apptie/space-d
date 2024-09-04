package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.exception.InvalidProfileImageNameException;
import java.util.Arrays;
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
class ProfileImageNameTest {

    @Test
    void findRandom_메서드는_호출하면_랜덤한_ProflieImageName를_반환한다() {
        // when & then
        assertDoesNotThrow(ProfileImageName::findRandom);
    }

    @ParameterizedTest
    @MethodSource("findByTestWithProfileImageKoreanName")
    void fidnBy_메서드는_유효한_이름을_전달하면_그에_맞는_ProfileImageName을_반환한다(String korean) {
        // when
        ProfileImageName actual = ProfileImageName.findBy(korean);

        // then
        assertThat(actual.getKorean()).isEqualTo(korean);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findBy_메서드는_유효하지_않은_이름을_전달하면_InvalidProfileImageNameException_예외가_발생한다(String korean) {
        // when & then
        assertThatThrownBy(() -> ProfileImageName.findBy(korean))
                .isInstanceOf(InvalidProfileImageNameException.class)
                .hasMessageContaining("잘못된 프로필 이미지 이름");
    }

    private static Stream<Arguments> findByTestWithProfileImageKoreanName() {
        return Arrays.stream(ProfileImageName.values())
                     .map(ProfileImageName::getKorean)
                     .map(Arguments::of);
    }
}
