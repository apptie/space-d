package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.embed.CareerInfo;
import com.dnd.spaced.core.account.domain.enums.Company;
import com.dnd.spaced.core.account.domain.enums.Experience;
import com.dnd.spaced.core.account.domain.enums.JobGroup;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.exception.InvalidIdException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidJobGroupException;
import com.dnd.spaced.core.account.domain.embed.exception.InvalidNicknameException;
import com.dnd.spaced.core.account.domain.embed.exception.InvalidProfileImageException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidRoleNameException;
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
                             .id("user1@naver.com")
                             .nickname("재빠른지구001")
                             .profileImage("earth.png")
                             .roleName("ROLE_USER")
                             .build()
        );
    }

    @ParameterizedTest(name = " {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원을_초기화할_때_식별자가_없다면_회원을_초기화할_수_없다(String invalidId) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .id(invalidId)
                             .nickname("재빠른지구001")
                             .profileImage("earth.png")
                             .roleName("ROLE_USER")
                             .build()
        ).isInstanceOf(InvalidIdException.class)
         .hasMessageContaining("ID는 null이나 비어 있을 수 없습니다.");
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
    void 회원을_초기화할_때_비어_있거나_유효한_길이의_닉네임이_아니라면_회원을_초기화할_수_없다(String invalidNickname) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .id("user1@naver.com")
                             .nickname(invalidNickname)
                             .profileImage("earth.png")
                             .roleName("ROLE_USER")
                             .build()
        ).isInstanceOf(InvalidNicknameException.class)
         .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원을_초기화할_때_비어_있는_프로필_이미지_경로라면_회원을_초기화할_수_없다(String invalidProfileImage) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .id("user1@naver.com")
                             .nickname("재빠른지구001")
                             .profileImage(invalidProfileImage)
                             .roleName("ROLE_USER")
                             .build()
        ).isInstanceOf(InvalidProfileImageException.class)
         .hasMessage("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
    }

    @ParameterizedTest(name = "권한 정보가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원을_초기화할_때_유효한_권한_정보가_아니라면_회원을_초기화할_수_없다(String invalidRoleName) {
        // when & then
        assertThatThrownBy(
                () -> Account.builder()
                             .id("user1@naver.com")
                             .nickname("재빠른지구001")
                             .profileImage("earth.png")
                             .roleName(invalidRoleName)
                             .build()
        ).isInstanceOf(InvalidRoleNameException.class)
         .hasMessageContaining("잘못된 권한 정보 이름");
    }

    @Test
    void 회원의_경력_정보를_변경한다() {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        // when
        account.changeCareerInfo("개발자", "비공개", "1~2년 차");

        // then
        CareerInfo careerInfo = account.getCareerInfo();

        assertAll(
                () -> assertThat(careerInfo.getCompany()).isEqualTo(Company.BLIND),
                () -> assertThat(careerInfo.getJobGroup()).isEqualTo(JobGroup.DEVELOP),
                () -> assertThat(careerInfo.getExperience()).isEqualTo(Experience.BETWEEN_FIRST_SECOND)
        );
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_회사명이_아니라면_경력_정보를_변경할_수_없다(String invalidCompanyName) {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareerInfo(
                        "개발자",
                        invalidCompanyName,
                        "1~2년 차"
                )

        ).isInstanceOf(InvalidCompanyException.class)
         .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "직군 이름이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_직군_이름이_아니라면_경력_정보를_변경할_수_없다(String invalidJobGroupName) {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareerInfo(
                        invalidJobGroupName,
                        "비공개",
                        "1~2년 차"
                )

        ).isInstanceOf(InvalidJobGroupException.class)
         .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_경력이_아니라면_경력_정보를_변경할_수_없다(String invalidExperienceName) {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        // when & then
        assertThatThrownBy(
                () -> account.changeCareerInfo(
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
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        // when
        String changedNickname = "행복한화성001";
        String changedProfileImage = "mars.png";

        account.changeProfileInfo(changedNickname, changedProfileImage);

        // then
        assertAll(
                () -> assertThat(account.getProfileInfo().getNickname()).isEqualTo(changedNickname),
                () -> assertThat(account.getProfileInfo().getProfileImage()).isEqualTo(changedProfileImage)
        );
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원의_프로필_정보_변경_시_프로필_이미지_경로가_비어_있으면_프로필_정보를_변환할_수_없다(String invalidProfileImage) {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
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
    void 회원의_프로필_정보_변경_시_비어_있거나_유효한_길이가_아닌_닉네임이면_프로필_정보를_변환할_수_없다(String invalidNickname) {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        // when & then
        assertThatThrownBy(() -> account.changeProfileInfo(invalidNickname, "mars.png"))
                .isInstanceOf(InvalidNicknameException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 10글자 이하여야 합니다.");
    }

    @Test
    void 회원_식별자를_반환한다() {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
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
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        // when
        boolean actual = account.isNew();

        // then
        assertThat(actual).isTrue();
    }

    private static Stream<Object> isEqualToTestArguments() {
        return Stream.of(
                Arguments.of("user1@naver.com", true),
                Arguments.of("user2@naver.com", false)
        );
    }

    @ParameterizedTest(name = "회원의 식별자를 {0}과 비교하면 {1}을 반환한다")
    @MethodSource("isEqualToTestArguments")
    void 회원의_식별자가_일치하는지_비교한다(String id, boolean expected) {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        // when
        boolean actual = account.isEqualTo(id);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
