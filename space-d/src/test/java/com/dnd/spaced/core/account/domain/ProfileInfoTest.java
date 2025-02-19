package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.exception.InvalidNicknameException;
import com.dnd.spaced.core.account.domain.exception.InvalidProfileImageException;
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
class ProfileInfoTest {

    @Test
    void 프로필_정보를_초기화한다() {
        // when & then
        assertDoesNotThrow(() -> new ProfileInfo("재빠른지구001", "earth.png"));
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
    void 프로필_정보를_초기화할_때_유효한_길이의_닉네임이_아니라면_프로필_정보를_초기화할_수_없다(String invalidNickname) {
        // when & then
        assertThatThrownBy(() -> new ProfileInfo(invalidNickname, "earth.png"))
                .isInstanceOf(InvalidNicknameException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 프로필 정보를 초기화할 수 없다")
    @NullAndEmptySource
    void 프로필_정보를_초기화할_때_비어_있는_프로필_이미지_경로라면_프로필_정보를_초기화할_수_없다(String invalidProfileImage) {
        // when & then
        assertThatThrownBy(() -> new ProfileInfo("행복한지구001", invalidProfileImage))
                .isInstanceOf(InvalidProfileImageException.class)
                .hasMessage("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
    }

    @Test
    void 프로필_정보를_변경한다() {
        // given
        ProfileInfo profileInfo = new ProfileInfo("재빠른지구001", "earth.png");

        // when
        String changedNickname = "행복한화성001";
        String changedProfileImage = "mars.png";

        profileInfo.changeProfileInfo(changedNickname, changedProfileImage);

        // then
        assertAll(
                () -> assertThat(profileInfo.getNickname()).isEqualTo(changedNickname),
                () -> assertThat(profileInfo.getProfileImage()).isEqualTo(changedProfileImage)
        );
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 프로필 정보를 변경할 수 없다")
    @NullAndEmptySource
    void 프로필_정보를_변경할_때_비어_있는_프로필_이미지_경로라면_프로필_정보를_변경할_수_없다(String invalidProfileImage) {
        // given
        ProfileInfo profileInfo = new ProfileInfo("재빠른지구001", "earth.png");

        // when & then
        assertThatThrownBy(() -> profileInfo.changeProfileInfo("행복한화성001", invalidProfileImage))
                .isInstanceOf(InvalidProfileImageException.class)
                .hasMessage("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
    }

    private static Stream<Arguments> changeProfileInfoTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of("  "),
                Arguments.of("1234"),
                Arguments.of("12345678901")
        );
    }

    @ParameterizedTest(name = "닉네임이 {0}일 때 프로필 정보를 변경할 수 없다")
    @MethodSource("changeProfileInfoTestWithInvalidNickname")
    void 프로필_정보를_변경할_때_유효한_닉네임_길이가_아니라면_프로필_정보를_변경할_수_없다(String invalidNickname) {
        // given
        ProfileInfo profileInfo = new ProfileInfo("nickname", "profileImage");

        // when & then
        assertThatThrownBy(() -> profileInfo.changeProfileInfo(invalidNickname, "profileImage"))
                .isInstanceOf(InvalidNicknameException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }
}
