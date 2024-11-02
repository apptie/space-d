package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.Company;
import com.dnd.spaced.core.account.domain.Experience;
import com.dnd.spaced.core.account.domain.JobGroup;
import com.dnd.spaced.core.account.domain.Role;
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
        String id = "email@email.com";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertDoesNotThrow(
                () -> initAccountInfoService.initCareerInfo(
                        id,
                        JobGroup.DESIGN.getName(),
                        Company.BLIND.getName(),
                        Experience.BETWEEN_THIRD_FOURTH.getName()
                )
        );
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보_초기화_시_유효한_회사명이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidCompanyName) {
        // given
        String id = "email@email.com";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        id,
                        JobGroup.DEVELOP.getName(),
                        invalidCompanyName,
                        Experience.BLIND.getName()
                )

        ).isInstanceOf(InvalidCompanyException.class)
         .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "직군이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보_초기화_시_유효한_직군이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidJobGroupName) {
        // given
        String id = "email@email.com";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        id,
                        invalidJobGroupName,
                        Company.BLIND.getName(),
                        Experience.BLIND.getName()
                )

        ).isInstanceOf(InvalidJobGroupException.class)
         .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보_초기화_시_유효한_경력이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidExperienceName) {
        // given
        String id = "email@email.com";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName(Role.ROLE_USER.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        id,
                        JobGroup.DEVELOP.getName(),
                        Company.BLIND.getName(),
                        invalidExperienceName
                )

        ).isInstanceOf(InvalidExperienceException.class)
         .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 경력_정보_초기화_시_유효한_회원_식별자가_아닌_경우_경력_정보를_초기화할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        "email@email.com",
                        JobGroup.DESIGN.getName(),
                        Company.BLIND.getName(),
                        Experience.BETWEEN_THIRD_FOURTH.getName()
                )
        ).isInstanceOf(ForbiddenInitCareerInfoException.class)
         .hasMessage("최초로 가입한 회원이 아닙니다.");
    }
}
