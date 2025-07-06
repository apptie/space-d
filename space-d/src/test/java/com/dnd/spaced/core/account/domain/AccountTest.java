package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.embed.Career;
import com.dnd.spaced.core.account.domain.embed.exception.InvalidNicknameException;
import com.dnd.spaced.core.account.domain.embed.exception.InvalidProfileImageException;
import com.dnd.spaced.core.account.domain.enums.Company;
import com.dnd.spaced.core.account.domain.enums.Experience;
import com.dnd.spaced.core.account.domain.enums.JobGroup;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidJobGroupException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountTest {

    @Test
    void 회원을_초기화한다() {
        // when & then
        Account actual = assertDoesNotThrow(
                () -> Account.builder()
                             .registrationId(RegistrationId.KAKAO)
                             .socialIdentifier("12345")
                             .nickname("재빠른지구001")
                             .profileImage("earth.png")
                             .role(Role.ROLE_USER)
                             .build()
        );

        assertAll(
                () -> assertThat(actual.getSocial().getRegistrationId()).isEqualTo(RegistrationId.KAKAO),
                () -> assertThat(actual.getSocial().getSocialId()).isEqualTo("12345"),
                () -> assertThat(actual.getProfile().getNickname()).isEqualTo("재빠른지구001"),
                () -> assertThat(actual.getProfile().getProfileImage()).isEqualTo("earth.png"),
                () -> assertThat(actual.getRole()).isEqualTo(Role.ROLE_USER)
        );
    }

    private static Stream<Arguments> builderTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of("  "),
                Arguments.of("1234"),
                Arguments.of("12345678901")
        );
    }

    @ParameterizedTest(name = "닉네임이 {0}일 때 예외가 발생한다")
    @MethodSource("builderTestWithInvalidNickname")
    void 비어_있거나_유효한_길이의_닉네임이_아니라면_회원을_초기화할_수_없다(String invalidNickname) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .registrationId(RegistrationId.KAKAO)
                             .socialIdentifier("12345")
                             .nickname(invalidNickname)
                             .profileImage("earth.png")
                             .role(Role.ROLE_USER)
                             .build()
        ).isInstanceOf(InvalidNicknameException.class)
         .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 비어_있는_프로필_이미지_경로라면_회원을_초기화할_수_없다(String invalidProfileImage) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .registrationId(RegistrationId.KAKAO)
                             .socialIdentifier("12345")
                             .nickname("재빠른지구001")
                             .profileImage(invalidProfileImage)
                             .role(Role.ROLE_USER)
                             .build()
        ).isInstanceOf(InvalidProfileImageException.class)
         .hasMessage("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
    }

    @Test
    void 회원의_경력_정보를_변경한다() {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        // when
        account.changeCareer("개발자", "비공개", "1~2년 차");

        // then
        Career career = account.getCareer();

        assertAll(
                () -> assertThat(career.getCompany()).isEqualTo(Company.BLIND),
                () -> assertThat(career.getJobGroup()).isEqualTo(JobGroup.DEVELOP),
                () -> assertThat(career.getExperience()).isEqualTo(Experience.BETWEEN_FIRST_SECOND)
        );
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 유효한_회사명이_아니라면_회원_경력_정보를_변경할_수_없다(String invalidCompanyName) {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareer(
                        "개발자",
                        invalidCompanyName,
                        "1~2년 차"
                )

        ).isInstanceOf(InvalidCompanyException.class)
         .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "직군 이름이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 유효한_직군_이름이_아니라면_회원_경력_정보를_변경할_수_없다(String invalidJobGroupName) {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareer(
                        invalidJobGroupName,
                        "비공개",
                        "1~2년 차"
                )

        ).isInstanceOf(InvalidJobGroupException.class)
         .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 유효한_경력이_아니라면_회원_경력_정보를_변경할_수_없다(String invalidExperienceName) {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareer(
                        "개발자",
                        "비공개",
                        invalidExperienceName
                )

        ).isInstanceOf(InvalidExperienceException.class)
         .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 회원의_프로필_정보를_변경한다() {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        // when
        String changedNickname = "행복한화성001";
        String changedProfileImage = "mars.png";

        account.changeProfileInfo(changedNickname, changedProfileImage);

        // then
        assertAll(
                () -> assertThat(account.getProfile().getNickname()).isEqualTo(changedNickname),
                () -> assertThat(account.getProfile().getProfileImage()).isEqualTo(changedProfileImage)
        );
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 프로필_이미지_경로가_비어_있으면_회원_프로필_정보를_변환할_수_없다(String invalidProfileImage) {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        // when & then
        assertThatThrownBy(() -> account.changeProfileInfo("행복한화성001", invalidProfileImage))
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

    @ParameterizedTest(name = "닉네임이 {0}일 때 예외가 발생한다")
    @MethodSource("changeProfileInfoTestWithInvalidNickname")
    void 비어_있거나_유효한_길이가_아닌_닉네임이면_프로필_정보를_변환할_수_없다(String invalidNickname) {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        // when & then
        assertThatThrownBy(() -> account.changeProfileInfo(invalidNickname, "mars.png"))
                .isInstanceOf(InvalidNicknameException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    private static Stream<Object> isEqualToTestArguments() {
        return Stream.of(
                Arguments.of(1L, true),
                Arguments.of(2L, false)
        );
    }

    @ParameterizedTest(name = "회원의 식별자를 {0}과 비교하면 {1}을 반환한다")
    @MethodSource("isEqualToTestArguments")
    void 회원의_식별자가_일치하는지_비교한다(Long id, boolean expected) {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();
        ReflectionTestUtils.setField(account, "id", 1L);

        // when
        boolean actual = account.isEqualTo(id);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
