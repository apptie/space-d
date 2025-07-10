package com.dnd.spaced.core.account.domain.embed;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.embed.exception.InvalidNicknameException;
import com.dnd.spaced.core.account.domain.embed.exception.InvalidProfileImageException;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProfileTest {

    @Test
    void 프로필_정보를_초기화한다() {
        // when & then
        assertDoesNotThrow(() -> Profile.of("재빠른지구001", ProfileImageName.EARTH));
    }

    private static Stream<Arguments> constructorTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of("  "),
                Arguments.of("1234"),
                Arguments.of("12345678901")
        );
    }

    @ParameterizedTest(name = "닉네임이 {0}일 때 프로필 정보를 초기화할 수 없다")
    @MethodSource("constructorTestWithInvalidNickname")
    void 유효한_길이의_닉네임이_아니라면_프로필_정보를_초기화할_수_없다(String invalidNickname) {
        // when & then
        assertThatThrownBy(() -> Profile.of(invalidNickname, ProfileImageName.EARTH))
                .isInstanceOf(InvalidNicknameException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    @Test
    void 비어_있는_프로필_이미지_경로라면_프로필_정보를_초기화할_수_없다() {
        // when & then
        assertThatThrownBy(() -> Profile.of("행복한지구001", null))
                .isInstanceOf(InvalidProfileImageException.class)
                .hasMessage("프로필 이미지 정보는 null일 수 없습니다.");
    }
}
