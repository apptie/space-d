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
    void build_메서드는_유효한_id_nickname_profileImage_roleName을_전달하면_Account를_초기화하고_반환한다() {
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

    @ParameterizedTest
    @NullAndEmptySource
    void build_메서드는_유효하지_않은_id을_전달하면_InvalidEmailException_예외가_발생한다(String invalidId) {
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

    @ParameterizedTest(name = "닉네임이 {0}일 때 예외가 발생한다")
    @MethodSource("builderTestWithInvalidNickname")
    void build_메서드는_유효하지_않은_nickname을_전달하면_InvalidNicknameException_예외가_발생한다(String invalidNickname) {
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

    @ParameterizedTest
    @NullAndEmptySource
    void build_메서드는_유효하지_않은_profileImage를_전달하면_InvalidProfileImageException_예외가_발생한다(String invalidProfileImage) {
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

    @ParameterizedTest
    @NullAndEmptySource
    void build_메서드는_유효하지_않은_roleName을_전달하면_InvalidRoleNameException_예외가_발생한다(String invalidRoleName) {
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
    void changeCareerInfo_메서드는_유효한_jobGroupName_companyName_experienceName을_전달하면_CareerInfo를_변경한다() {
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

    @ParameterizedTest
    @NullAndEmptySource
    void changeCareerInfo_메서드는_유효하지_않은_companyName을_전달하면_InvalidCompanyException_예외가_발생한다(String invalidCompanyName) {
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

    @ParameterizedTest
    @NullAndEmptySource
    void changeCareerInfo_메서드는_유효하지_않은_jobGroupName을_전달하면_InvalidJobGroupException_예외가_발생한다(String invalidJobGroupName) {
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

    @ParameterizedTest
    @NullAndEmptySource
    void changeCareerInfo_메서드는_유효하지_않은_experienceName을_전달하면_InvalidExperienceException_예외가_발생한다(String invalidExperienceName) {
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
    void changeProfile_메서드는_유효한_changedNickname_changedProfileImage를_전달하면_회원_정보를_변경한다() {
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

    @ParameterizedTest
    @NullAndEmptySource
    void changeProfile_메서드는_유효하지_않은_profileImage를_전달하면_InvalidProfileImageException_예외가_발생한다(String invalidProfileImage) {
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

    @ParameterizedTest
    @MethodSource("changeProfileInfoTestWithInvalidNickname")
    void changeProfile_메서드는_유효하지_않은_nickname을_전달하면_InvalidNicknameException_예외가_발생한다(String invalidNickname) {
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
    void getId_메서드는_id를_반환한다() {
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
    void isNew_메서드는_Account가_영속화되었는지_여부를_반환한다() {
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

    @Test
    void isEqualTo_메서드는_일치하는_id를_전달하면_true를_반환한다() {
        // given
        String id = "email";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when
        boolean actual = account.isEqualTo(id);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void isEqualTo_메서드는_일치하지_않는_id를_전달하면_false를_반환한다() {
        // given
        Account account = Account.builder()
                                 .id("email")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        // when
        boolean actual = account.isEqualTo("invalidId");

        // then
        assertThat(actual).isFalse();
    }

    private static Stream<Arguments> builderTestWithInvalidNickname() {
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
