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
    void 생성자는_유효한_nickname_profileImage를_전달하면_ProfileInfo를_초기화하고_반환한다() {
        // when & then
        assertDoesNotThrow(() -> new ProfileInfo("nickname", "profileInfo"));
    }

    @ParameterizedTest(name = "닉네임이 {0}일 때 예외가 발생한다")
    @MethodSource("constructorTestWithInvalidNickname")
    void 생성자는_유효하지_않은_nickname을_전달하면_InvalidNicknameException_예외가_발생한다(String invalidNickname) {
        // when & then
        assertThatThrownBy(() -> new ProfileInfo(invalidNickname, "profileInfo"))
                .isInstanceOf(InvalidNicknameException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성자는_유효하지_않은_profileImage를_전달하면_InvalidProfileImageException_예외가_발생한다(String invalidProfileImage) {
        // when & then
        assertThatThrownBy(() -> new ProfileInfo("nickname", invalidProfileImage))
                .isInstanceOf(InvalidProfileImageException.class)
                .hasMessage("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
    }

    @Test
    void changeProfile_메서드는_유효한_changedNickname_changedProfileImage를_전달하면_회원_정보를_변경한다() {
        // given
        ProfileInfo profileInfo = new ProfileInfo("nickname", "profileImage");

        // when
        String changedNickname = "changeNick";
        String changedProfileImage = "changeProfileImage";

        profileInfo.changeProfileInfo(changedNickname, changedProfileImage);

        // then
        assertAll(
                () -> assertThat(profileInfo.getNickname()).isEqualTo(changedNickname),
                () -> assertThat(profileInfo.getProfileImage()).isEqualTo(changedProfileImage)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void changeProfile_메서드는_유효하지_않은_profileImage를_전달하면_InvalidProfileImageException_예외가_발생한다(String invalidProfileImage) {
        // given
        ProfileInfo profileInfo = new ProfileInfo("nickname", "profileImage");

        // when & then
        assertThatThrownBy(() -> profileInfo.changeProfileInfo("nickname", invalidProfileImage))
                .isInstanceOf(InvalidProfileImageException.class)
                .hasMessage("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("changeProfileInfoTestWithInvalidNickname")
    void changeProfile_메서드는_유효하지_않은_nickname을_전달하면_InvalidNicknameException_예외가_발생한다(String invalidNickname) {
        // given
        ProfileInfo profileInfo = new ProfileInfo("nickname", "profileImage");

        // when & then
        assertThatThrownBy(() -> profileInfo.changeProfileInfo(invalidNickname, "profileImage"))
                .isInstanceOf(InvalidNicknameException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    private static Stream<Arguments> constructorTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null), Arguments.of(""), Arguments.of("  "),
                Arguments.of("aaaa"), Arguments.of("aaaaaaaaaaa")
        );
    }

    private static Stream<Arguments> changeProfileInfoTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null), Arguments.of(""), Arguments.of("  "),
                Arguments.of("aaaa"), Arguments.of("aaaaaaaaaaa")
        );
    }
}
