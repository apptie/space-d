package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.exception.InvalidJobGroupException;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.auth.application.exception.ForbiddenInitCareerInfoException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
class InitAccountInfoServiceTest {

    @Autowired
    InitAccountInfoService initAccountInfoService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void 경력_정보를_초기화한다() {
        // given
        String id = "user1@naver.com";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when & then
        assertDoesNotThrow(
                () -> initAccountInfoService.initCareerInfo(
                        id,
                        "개발자",
                        "비공개",
                        "1~2년 차"
                )
        );
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보_초기화_시_유효한_회사명이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidCompanyName) {
        // given
        String id = "user1@naver.com";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        id,
                        "개발자",
                        invalidCompanyName,
                        "1~2년 차"
                )

        ).isInstanceOf(InvalidCompanyException.class)
         .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "직군이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보_초기화_시_유효한_직군이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidJobGroupName) {
        // given
        String id = "user1@naver.com";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        id,
                        invalidJobGroupName,
                        "비공개",
                        "1~2년 차"
                )

        ).isInstanceOf(InvalidJobGroupException.class)
         .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보_초기화_시_유효한_경력이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidExperienceName) {
        // given
        String id = "user1@naver.com";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        id,
                        "개발자",
                        "비공개",
                        invalidExperienceName
                )

        ).isInstanceOf(InvalidExperienceException.class)
         .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 경력_정보_초기화_시_회원_식별자가_없거나_탈퇴한_경우_경력_정보를_초기화할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        "user1@naver.com",
                        "개발자",
                        "비공개",
                        "1~2년 차"
                )
        ).isInstanceOf(ForbiddenInitCareerInfoException.class)
         .hasMessage("최초로 가입한 회원이 아닙니다.");
    }
}
