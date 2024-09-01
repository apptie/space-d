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
    void initCareerInfo_메서드는_유효한_jobGroupName_companyName_experienceName을_전달하면_CareerInfo를_초기화한다() {
        // given
        String id = "id";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
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

    @ParameterizedTest
    @NullAndEmptySource
    void initCareerInfo_메서드는_유효하지_않은_companyName을_전달하면_InvalidCompanyException_예외가_발생한다(String invalidCompanyName) {
        // given
        String id = "id";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
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

    @ParameterizedTest
    @NullAndEmptySource
    void initCareerInfo_메서드는_유효하지_않은_jobGroupName을_전달하면_InvalidJobGroupException_예외가_발생한다(String invalidJobGroupName) {
        // given
        String id = "id";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
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

    @ParameterizedTest
    @NullAndEmptySource
    void initCareerInfo_메서드는_유효하지_않은_experienceName을_전달하면_InvalidExperienceException_예외가_발생한다(String invalidExperienceName) {
        // given
        String id = "id";
        Account account = Account.builder()
                                 .id(id)
                                 .nickname("nickname")
                                 .profileImage("profileImage")
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
    void initCareerInfo_메서드는_유효하지_않은_id를_전달한_경우_ForbiddenInitCareerInfoException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(
                () -> initAccountInfoService.initCareerInfo(
                        "id",
                        JobGroup.DESIGN.getName(),
                        Company.BLIND.getName(),
                        Experience.BETWEEN_THIRD_FOURTH.getName()
                )
        ).isInstanceOf(ForbiddenInitCareerInfoException.class)
         .hasMessage("로그인이 필요한 기능입니다.");
    }
}
