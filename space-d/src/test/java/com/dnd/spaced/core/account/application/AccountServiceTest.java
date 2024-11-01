package com.dnd.spaced.core.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.application.dto.response.AccountInfoDto;
import com.dnd.spaced.core.account.application.exception.ForbiddenAccountException;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.Company;
import com.dnd.spaced.core.account.domain.Experience;
import com.dnd.spaced.core.account.domain.JobGroup;
import com.dnd.spaced.core.account.domain.ProfileImageName;
import com.dnd.spaced.core.account.domain.Role;
import com.dnd.spaced.core.account.domain.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.exception.InvalidJobGroupException;
import com.dnd.spaced.core.account.domain.exception.InvalidProfileImageNameException;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@CleanUpDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void 지정한_회원을_탈퇴_처리한다() {
        // given
        String accountId = "email";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when
        accountService.withdrawal(accountId);

        // then
        assertThat(accountRepository.findBy(accountId)).isEmpty();
    }

    @Test
    void 탈퇴_시_유효한_회원_식별자가_아니라면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> accountService.withdrawal("id"))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    @Test
    void 회원의_경력_정보를_변경한다() {
        // given
        String accountId = "email";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertDoesNotThrow(
                () -> accountService.changeCareerInfo(
                        accountId,
                        JobGroup.DEVELOP.getName(),
                        Company.BLIND.getName(),
                        Experience.BETWEEN_FIRST_SECOND.getName()
                )
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_직군_이름이_아니라면_예외가_발생한다(String invalidJobGroupName) {
        // given
        String accountId = "email";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> accountService.changeCareerInfo(
                        accountId,
                        invalidJobGroupName,
                        Company.BLIND.getName(),
                        Experience.BETWEEN_FIRST_SECOND.getName()
                )
        ).isInstanceOf(InvalidJobGroupException.class)
         .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_회사명이_아니라면_예외가_발생한다(String invalidCompanyName) {
        // given
        String accountId = "email";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> accountService.changeCareerInfo(
                        accountId,
                        JobGroup.DEVELOP.getName(),
                        invalidCompanyName,
                        Experience.BETWEEN_FIRST_SECOND.getName()
                )
        ).isInstanceOf(InvalidCompanyException.class)
         .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원의_경력_정보_변경_시_유효한_경력이_아니라면_예외가_발생한다(String invalidExperienceName) {
        // given
        String accountId = "email";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> accountService.changeCareerInfo(
                        accountId,
                        JobGroup.DEVELOP.getName(),
                        Company.BLIND.getName(),
                        invalidExperienceName
                )
        ).isInstanceOf(InvalidExperienceException.class)
         .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 회원의_경력_정보_변경_시_유효한_회원_식별자가_아니라면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(
                () -> accountService.changeCareerInfo(
                        "id",
                        JobGroup.DEVELOP.getName(),
                        Company.BLIND.getName(),
                        Experience.BETWEEN_FIRST_SECOND.getName()
                )
        ).isInstanceOf(ForbiddenAccountException.class)
         .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    private static Stream<Arguments> changeProfileInfoTestWithProfileImageKoreanName() {
        return Arrays.stream(ProfileImageName.values())
                     .map(ProfileImageName::getKorean)
                     .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("changeProfileInfoTestWithProfileImageKoreanName")
    void 회원의_프로필_정보를_변경한다(String profileImageKoreanName) {
        // given
        String accountId = "email";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertDoesNotThrow(
                () -> accountService.changeProfileInfo(
                        accountId,
                        account.getProfileInfo().getNickname(),
                        profileImageKoreanName
                )
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원의_프로필_정보_변경_시_유효한_프로필_이미지가_아니라면_예외가_발생한다(String invalidProfileImageKoreanName) {
        // given
        String accountId = "email";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> accountService.changeProfileInfo(
                        accountId,
                        account.getProfileInfo().getNickname(),
                        invalidProfileImageKoreanName
                )
        ).isInstanceOf(InvalidProfileImageNameException.class)
         .hasMessageContaining("못된 프로필 이미지 이름");
    }

    @Test
    void 회원의_프로필_정보_변경_시_유효한_회원_식별자가_아니라면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(
                () -> accountService.changeProfileInfo(
                        "withdrawalEmail",
                        "nickname",
                        ProfileImageName.EARTH.getKorean()
                )
        ).isInstanceOf(ForbiddenAccountException.class)
         .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    @Test
    void 회원의_정보를_찾아서_반환한다() {
        // given
        String accountId = "email";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        account.changeCareerInfo(JobGroup.DEVELOP.getName(), Company.BLIND.getName(), Experience.BLIND.getName());
        accountRepository.save(account);

        // when
        AccountInfoDto actual = accountService.findAccountInfo(accountId);

        // then
        assertAll(
                () -> assertThat(actual.companyName()).isEqualTo(account.getCareerInfo().getCompany().getName()),
                () -> assertThat(actual.jobGroupName()).isEqualTo(account.getCareerInfo().getJobGroup().getName()),
                () -> assertThat(actual.experienceName()).isEqualTo(account.getCareerInfo().getExperience().getName()),
                () -> assertThat(actual.profileImage()).isEqualTo(account.getProfileInfo().getProfileImage()),
                () -> assertThat(actual.nickname()).isEqualTo(account.getProfileInfo().getNickname())
        );
    }

    @Test
    void 회원_정보_조회_시_유효한_회원_식별자가_아니라면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> accountService.findAccountInfo("id"))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }
}
