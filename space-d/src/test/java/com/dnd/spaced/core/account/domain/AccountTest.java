package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.exception.InvalidIdException;
import com.dnd.spaced.core.account.domain.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.exception.InvalidJobGroupException;
import com.dnd.spaced.core.account.domain.exception.InvalidNicknameException;
import com.dnd.spaced.core.account.domain.exception.InvalidProfileImageException;
import com.dnd.spaced.core.account.domain.exception.InvalidRoleNameException;
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
class AccountTest {

    @Test
    void 회원을_초기화한다() {
        // when & then
        assertDoesNotThrow(
                () -> Account.builder()
                             .id("email")
                             .nickname("nickname")
                             .profileImage("profileImage")
                             .roleName(Role.ROLE_ADMIN.name())
                             .build()
        );
    }

    @ParameterizedTest(name = " {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원을_초기화할_때_유효한_회원_식별자가_아니라면_예외가_발생한다(String invalidId) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .id(invalidId)
                             .nickname("nickname")
                             .profileImage("profileImage")
                             .roleName(Role.ROLE_ADMIN.name())
                             .build()
        ).isInstanceOf(InvalidIdException.class)
         .hasMessageContaining("ID는 null이나 비어 있을 수 없습니다.");
    }

    private static Stream<Arguments> builderTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null), Arguments.of(""), Arguments.of("  "),
                Arguments.of("1234"), Arguments.of("12345678901")
        );
    }

    @ParameterizedTest(name = "닉네임이 {0}일 때 예외가 발생한다")
    @MethodSource("builderTestWithInvalidNickname")
    void 회원을_초기화할_때_유효한_닉네임이_아니라면_예외가_발생한다(String invalidNickname) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .id("email")
                             .nickname(invalidNickname)
                             .profileImage("profileImage")
                             .roleName(Role.ROLE_ADMIN.name())
                             .build()
        ).isInstanceOf(InvalidNicknameException.class)
         .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원을_초기화할_때_유효한_프로필_이미지가_아니라면_예외가_발생한다(String invalidProfileImage) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .id("email")
                             .nickname("nickname")
                             .profileImage(invalidProfileImage)
                             .roleName(Role.ROLE_ADMIN.name())
                             .build()
        ).isInstanceOf(InvalidProfileImageException.class)
         .hasMessage("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
    }

    @ParameterizedTest(name = "권한 정보가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원을_초기화할_때_유효한_권한_정보가_아니라면_예외가_발생한다(String invalidRoleName) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .id("email")
                             .nickname("nickname")
                             .profileImage("profileImage")
                             .roleName(invalidRoleName)
                             .build()
        ).isInstanceOf(InvalidRoleNameException.class)
         .hasMessageContaining("잘못된 role name");
    }

    @Test
    void 회원의_경력_정보를_변경한다() {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when
        Experience experience = Experience.BLIND;
        JobGroup jobGroup = JobGroup.DEVELOP;
        Company company = Company.BLIND;

        account.changeCareerInfo(jobGroup.getName(), company.getName(), experience.getName());

        // then
        CareerInfo careerInfo = account.getCareerInfo();

        assertAll(
                () -> assertThat(careerInfo.getCompany()).isEqualTo(company),
                () -> assertThat(careerInfo.getJobGroup()).isEqualTo(jobGroup),
                () -> assertThat(careerInfo.getExperience()).isEqualTo(experience)
        );
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_회사명이_아니라면_예외가_발생한다(String invalidCompanyName) {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareerInfo(
                        JobGroup.DEVELOP.getName(),
                        invalidCompanyName,
                        Experience.BLIND.getName()
                )

        ).isInstanceOf(InvalidCompanyException.class)
         .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "직군 이름이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_직군_이름이_아니라면_예외가_발생한다(String invalidJobGroupName) {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareerInfo(
                        invalidJobGroupName,
                        Company.BLIND.getName(),
                        Experience.BLIND.getName()
                )

        ).isInstanceOf(InvalidJobGroupException.class)
         .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_경력이_아니라면_예외가_발생한다(String invalidExperienceName) {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareerInfo(
                        JobGroup.DEVELOP.getName(),
                        Company.BLIND.getName(),
                        invalidExperienceName
                )

        ).isInstanceOf(InvalidExperienceException.class)
         .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 회원의_프로필_정보를_변경한다() {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when
        String changedNickname = "changeNick";
        String changedProfileImage = "changeProfileImage";

        account.changeProfileInfo(changedNickname, changedProfileImage);

        // then
        assertAll(
                () -> assertThat(account.getProfileInfo().getNickname()).isEqualTo(changedNickname),
                () -> assertThat(account.getProfileInfo().getProfileImage()).isEqualTo(changedProfileImage)
        );
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원의_프로필_정보_변경_시_유효한_프로필_이미지가_아니라면_예외가_발생한다(String invalidProfileImage) {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when & then
        assertThatThrownBy(() -> account.changeProfileInfo("nickname", invalidProfileImage))
                .isInstanceOf(InvalidProfileImageException.class)
                .hasMessage("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
    }

    private static Stream<Arguments> changeProfileInfoTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null), Arguments.of(""), Arguments.of("  "),
                Arguments.of("1234"), Arguments.of("12345678901")
        );
    }

    @ParameterizedTest(name = "닉네임이 {0}일 때 예외가 발생한다")
    @MethodSource("changeProfileInfoTestWithInvalidNickname")
    void 회원의_프로필_정보_변경_시_유효한_닉네임이_아니라면_예외가_발생한다(String invalidNickname) {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when & then
        assertThatThrownBy(() -> account.changeProfileInfo(invalidNickname, "profileImage"))
                .isInstanceOf(InvalidNicknameException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    @Test
    void 회원_식별자를_반환한다() {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when
        String actual = account.getId();

        // then
        assertThat(actual).isEqualTo(account.getId());
    }

    @Test
    void 회원의_영속화_여부를_반환한다() {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when
        boolean actual = account.isNew();

        // then
        assertThat(actual).isTrue();
    }

    private static Stream<Object> isEqualToTestArguments() {
        return Stream.of(
                Arguments.of("email", true),
                Arguments.of("notSameEmail", false)
        );
    }

    @ParameterizedTest(name = "회원의 식별자가 email인 도메인에 대해 {0}과 비교하면 {1}을 반환한다")
    @MethodSource("isEqualToTestArguments")
    void 회원의_식별자가_일치하는지_비교한다(String id, boolean expected) {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when
        boolean actual = account.isEqualTo(id);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
