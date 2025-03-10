package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidJobGroupException;
import com.dnd.spaced.core.auth.application.dto.request.InitAccountCareerInfoRequest;
import com.dnd.spaced.core.auth.application.exception.ForbiddenInitCareerInfoException;
import com.dnd.spaced.core.auth.application.helper.WithAccountTestHelper;
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
class WithAccountTestTest extends WithAccountTestHelper {

    @Autowired
    InitAccountCareerInfoService initAccountCareerInfoService;

    @Test
    void 경력_정보를_초기화한다() {
        // given
        InitAccountCareerInfoRequest request = new InitAccountCareerInfoRequest(
                "개발자",
                "비공개",
                "1~2년 차"
        );

        // when & then
        assertDoesNotThrow(() -> initAccountCareerInfoService.initCareerInfo(account.getId(), request));
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 경력 정보를 초기화할 수 없다")
    @NullAndEmptySource
    void 유효한_회사명이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidCompanyName) {
        // given
        InitAccountCareerInfoRequest request = new InitAccountCareerInfoRequest(
                "개발자",
                invalidCompanyName,
                "1~2년 차"
        );

        // when & then
        assertThatThrownBy(() -> initAccountCareerInfoService.initCareerInfo(account.getId(), request))
                .isInstanceOf(InvalidCompanyException.class)
                .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "직군이 {0}일 때 경력 정보를 초기화할 수 없다")
    @NullAndEmptySource
    void 유효한_직군이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidJobGroupName) {
        // given
        InitAccountCareerInfoRequest request = new InitAccountCareerInfoRequest(
                invalidJobGroupName,
                "비공개",
                "1~2년 차"
        );

        // when & then
        assertThatThrownBy(() -> initAccountCareerInfoService.initCareerInfo(account.getId(), request))
                .isInstanceOf(InvalidJobGroupException.class)
                .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 경력 정보를 초기화할 수 없다")
    @NullAndEmptySource
    void 유효한_경력이_아닌_경우_경력_정보를_초기화할_수_없다(String invalidExperienceName) {
        // given
        InitAccountCareerInfoRequest request = new InitAccountCareerInfoRequest(
                "개발자",
                "비공개",
                invalidExperienceName
        );

        // when & then
        assertThatThrownBy(() -> initAccountCareerInfoService.initCareerInfo(account.getId(), request))
                .isInstanceOf(InvalidExperienceException.class)
                .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 회원_ID가_없거나_이미_탈퇴한_경우_경력_정보를_초기화할_수_없다() {
        // given
        InitAccountCareerInfoRequest request = new InitAccountCareerInfoRequest(
                "개발자",
                "비공개",
                "1~2년 차"
        );

        // when & then
        assertThatThrownBy(() -> initAccountCareerInfoService.initCareerInfo(-999L, request))
                .isInstanceOf(ForbiddenInitCareerInfoException.class)
                .hasMessage("최초로 가입한 회원이 아닙니다.");
    }
}
