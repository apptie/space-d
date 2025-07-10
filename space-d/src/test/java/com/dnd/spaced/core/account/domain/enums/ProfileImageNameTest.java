package com.dnd.spaced.core.account.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.enums.exception.InvalidProfileImageNameException;
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
    void 정해진_프로필_이미지_이름_중_랜덤한_프로필_이미지_이름을_반환한다() {
        // when & then
        assertDoesNotThrow(ProfileImageName::findRandom);
    }

    private static Stream<Arguments> findByKoreanTestWithKoreanName() {
        return Stream.of(
                Arguments.of("수성", ProfileImageName.MERCURY),
                Arguments.of("금성", ProfileImageName.VENUS),
                Arguments.of("지구", ProfileImageName.EARTH),
                Arguments.of("화성", ProfileImageName.MARS),
                Arguments.of("목성", ProfileImageName.JUPITER),
                Arguments.of("토성", ProfileImageName.SATURN),
                Arguments.of("천왕성", ProfileImageName.URANUS),
                Arguments.of("해왕성", ProfileImageName.NEPTUNE)
        );
    }

    @ParameterizedTest(name = "프로필 이미지의 한글 이름이 {0}일 때 {1}을 반환한다")
    @MethodSource("findByKoreanTestWithKoreanName")
    void 한글_이름으로_프로필_이미지를_찾는다(String korean, ProfileImageName expected) {
        // when
        ProfileImageName actual = ProfileImageName.findByKorean(korean);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유효한_한글_이름이_아니라면_프로필_이미지를_찾을_수_없다(String invalidKoreanName) {
        // when & then
        assertThatThrownBy(() -> ProfileImageName.findByKorean(invalidKoreanName))
                .isInstanceOf(InvalidProfileImageNameException.class)
                .hasMessageContaining("잘못된 프로필 이미지 이름");
    }

    private static Stream<Arguments> findByImageNameTestWithImageName() {
        return Stream.of(
                Arguments.of("mercury.png", ProfileImageName.MERCURY),
                Arguments.of("venus.png", ProfileImageName.VENUS),
                Arguments.of("earth.png", ProfileImageName.EARTH),
                Arguments.of("mars.png", ProfileImageName.MARS),
                Arguments.of("jupiter.png", ProfileImageName.JUPITER),
                Arguments.of("saturn.png", ProfileImageName.SATURN),
                Arguments.of("uranus.png", ProfileImageName.URANUS),
                Arguments.of("neptune.png", ProfileImageName.NEPTUNE)
        );
    }

    @ParameterizedTest(name = "프로필 이미지의 이미지 이름이 {0}일 때 {1}을 반환한다")
    @MethodSource("findByImageNameTestWithImageName")
    void 이미지_이름으로_프로필_이미지를_찾는다(String korean, ProfileImageName expected) {
        // when
        ProfileImageName actual = ProfileImageName.findByImageName(korean);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유효한_이미지_이름이_아니라면_프로필_이미지를_찾을_수_없다(String invalidImageName) {
        // when & then
        assertThatThrownBy(() -> ProfileImageName.findByKorean(invalidImageName))
                .isInstanceOf(InvalidProfileImageNameException.class)
                .hasMessageContaining("잘못된 프로필 이미지 이름");
    }
}
