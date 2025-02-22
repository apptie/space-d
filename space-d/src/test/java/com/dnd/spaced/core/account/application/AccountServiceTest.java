package com.dnd.spaced.core.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.application.dto.response.AccountInfoDto;
import com.dnd.spaced.core.account.application.exception.ForbiddenAccountException;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidJobGroupException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidProfileImageNameException;
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
        String accountId = "user1@naver.com";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when
        accountService.withdrawal(accountId);

        // then
        assertThat(accountRepository.findBy(accountId)).isEmpty();
    }

    @Test
    void 탈퇴_시_없거나_탈퇴한_회원_식별자라면_아니라면_탈퇴할_수_없다() {
        // given
        String accountId = "user1@naver.com";

        // when & then
        assertThatThrownBy(() -> accountService.withdrawal(accountId))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    @Test
    void 회원_경력_정보를_변경한다() {
        // given
        String accountId = "user1@naver.com";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when
        accountService.changeCareerInfo(
                accountId,
                "개발자",
                "비공개",
                "1~2년 차"
        );

        // then
        AccountInfoDto actual = accountService.findAccountInfo(accountId);

        assertAll(
                () -> assertThat(actual.jobGroupName()).isEqualTo("개발자"),
                () -> assertThat(actual.companyName()).isEqualTo("비공개"),
                () -> assertThat(actual.experienceName()).isEqualTo("1~2년 차")
        );
    }

    @ParameterizedTest(name = "직군 이름이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원_경력_정보_변경_시_유효한_직군_이름이_아니라면_경력_정보를_변경할_수_없다(String invalidJobGroupName) {
        // given
        String accountId = "user1@naver.com";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> accountService.changeCareerInfo(
                        accountId,
                        invalidJobGroupName,
                        "비공개",
                        "1~2년 차"
                )
        ).isInstanceOf(InvalidJobGroupException.class)
         .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원_경력_정보_변경_시_유효한_회사명이_아니라면_경력_정보를_변경할_수_없다(String invalidCompanyName) {
        // given
        String accountId = "user1@naver.com";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> accountService.changeCareerInfo(
                        accountId,
                        "개발자",
                        invalidCompanyName,
                        "1~2년 차"
                )
        ).isInstanceOf(InvalidCompanyException.class)
         .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원_경력_정보_변경_시_유효한_경력이_아니라면_경력_정보를_변경할_수_없다(String invalidExperienceName) {
        // given
        String accountId = "user1@naver.com";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> accountService.changeCareerInfo(
                        accountId,
                        "개발자",
                        "비공개",
                        invalidExperienceName
                )
        ).isInstanceOf(InvalidExperienceException.class)
         .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 회원_경력_정보_변경_시_지정한_식별자가_없거나_탈퇴한_회원이라면_경력_정보를_변경할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> accountService.changeCareerInfo(
                        "user1@naver.com",
                        "개발자",
                        "비공개",
                        "1~2년 차"
                )
        ).isInstanceOf(ForbiddenAccountException.class)
         .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    private static Stream<Arguments> changeProfileInfoTestWithProfileImageKoreanName() {
        return Arrays.stream(ProfileImageName.values())
                     .map(Arguments::of);
    }

    @ParameterizedTest(name = "프로필 이미지를 유효한 프로필 이미지 경로인 {0}으로 변경한다")
    @MethodSource("changeProfileInfoTestWithProfileImageKoreanName")
    void 회원_프로필_정보를_변경한다(ProfileImageName profileImageName) {
        // given
        String accountId = "user1@naver.com";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        account.changeCareerInfo("개발자", "비공개", "1~2년 차");
        accountRepository.save(account);

        // when
        accountService.changeProfileInfo(accountId, "행복한지구001", profileImageName.getKorean());

        // then
        AccountInfoDto actual = accountService.findAccountInfo(accountId);

        assertAll(
                () -> assertThat(actual.nickname()).isEqualTo("행복한지구001"),
                () -> assertThat(actual.profileImage()).isEqualTo(profileImageName.getImageName())
        );
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회원_프로필_정보_변경_시_프로필_이미지_경로가_비어_있으면_프로필_정보를_변경할_수_없다(String invalidProfileImageKoreanName) {
        // given
        String accountId = "user1@naver.com";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> accountService.changeProfileInfo(
                        accountId,
                        "재빠른지구001",
                        invalidProfileImageKoreanName
                )
        ).isInstanceOf(InvalidProfileImageNameException.class)
         .hasMessageContaining("잘못된 프로필 이미지 이름");
    }

    @Test
    void 회원_프로필_정보_변경_시_없거나_탈퇴한_회원_식별자라면_프로필_정보를_변경할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> accountService.changeProfileInfo(
                        "user1@naver.com",
                        "재빠른지구001",
                        "earth.png"
                )
        ).isInstanceOf(ForbiddenAccountException.class)
         .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    @Test
    void 회원_정보를_조회한다() {
        // given
        String accountId = "user1@naver.com";
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        account.changeCareerInfo("개발자", "비공개", "1~2년 차");
        accountRepository.save(account);

        // when
        AccountInfoDto actual = accountService.findAccountInfo(accountId);

        // then
        assertAll(
                () -> assertThat(actual.jobGroupName()).isEqualTo("개발자"),
                () -> assertThat(actual.companyName()).isEqualTo("비공개"),
                () -> assertThat(actual.experienceName()).isEqualTo("1~2년 차"),
                () -> assertThat(actual.nickname()).isEqualTo("재빠른지구001"),
                () -> assertThat(actual.profileImage()).isEqualTo("earth.png")
        );
    }

    @Test
    void 회원_정보_조회_시_없거나_탈퇴한_회원_식별자라면_회원_정보를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> accountService.findAccountInfo("user1@naver.com"))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }
}
