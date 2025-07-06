package com.dnd.spaced.core.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.application.dto.request.ChangeCareerRequest;
import com.dnd.spaced.core.account.application.dto.request.ChangeProfileRequest;
import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.core.account.application.exception.ForbiddenAccountException;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidJobGroupException;
import com.dnd.spaced.core.account.domain.enums.exception.InvalidProfileImageNameException;
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
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 지정한_회원을_탈퇴_처리한다() {
        // when & then
        assertDoesNotThrow(() -> accountService.withdrawal(1L));
    }

    @Test
    void 없거나_이미_탈퇴한_회원의_ID라면_아니라면_탈퇴할_수_없다() {
        // when & then
        assertThatThrownBy(() -> accountService.withdrawal(-999L))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 회원_경력_정보를_변경한다() {
        // given
        ChangeCareerRequest request = new ChangeCareerRequest(
                "개발자",
                "비공개",
                "1~2년 차"
        );

        // when
        accountService.changeCareerInfo(1L, request);

        // then
        AccountResponse actual = accountService.readAccount(1L);

        assertAll(
                () -> assertThat(actual.jobGroupName()).isEqualTo("개발자"),
                () -> assertThat(actual.companyName()).isEqualTo("비공개"),
                () -> assertThat(actual.experienceName()).isEqualTo("1~2년 차")
        );
    }

    @ParameterizedTest(name = "직군 이름이 {0}일 때 경력 정보를 변경할 수 없다.")
    @NullAndEmptySource // sql/cleanup.sql
    @Sql("classpath:sql/account/account.sql")
    void 유효한_직군_이름이_아니라면_경력_정보를_변경할_수_없다(String invalidJobGroupName) {
        // given
        ChangeCareerRequest request = new ChangeCareerRequest(
                invalidJobGroupName,
                "비공개",
                "1~2년 차"
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeCareerInfo(1L, request))
                .isInstanceOf(InvalidJobGroupException.class)
                .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 경력 정보를 변경할 수 없다.")
    @NullAndEmptySource
    @Sql("classpath:sql/account/account.sql")
    void 유효한_회사명이_아니라면_경력_정보를_변경할_수_없다(String invalidCompanyName) {
        // given
        ChangeCareerRequest request = new ChangeCareerRequest(
                "개발자",
                invalidCompanyName,
                "1~2년 차"
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeCareerInfo(1L, request))
                .isInstanceOf(InvalidCompanyException.class)
                .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 경력 정보를 변경할 수 없다.")
    @NullAndEmptySource
    @Sql("classpath:sql/account/account.sql")
    void 유효한_경력이_아니라면_경력_정보를_변경할_수_없다(String invalidExperienceName) {
        // given
        ChangeCareerRequest request = new ChangeCareerRequest(
                "개발자",
                "비공개",
                invalidExperienceName
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeCareerInfo(1L, request))
                .isInstanceOf(InvalidExperienceException.class)
                .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 없거나_탈퇴한_회원의_ID라면_경력_정보를_변경할_수_없다() {
        // given
        ChangeCareerRequest request = new ChangeCareerRequest(
                "개발자",
                "비공개",
                "1~2년 차"
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeCareerInfo(-999L, request))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    private static Stream<Arguments> changeProfileInfoTestWithProfileImageKoreanName() {
        return Arrays.stream(ProfileImageName.values())
                     .map(Arguments::of);
    }

    @ParameterizedTest(name = "프로필 이미지를 유효한 프로필 이미지 경로인 {0}으로 변경한다")
    @MethodSource("changeProfileInfoTestWithProfileImageKoreanName")
    @Sql("classpath:sql/account/account.sql")
    void 회원_프로필_정보를_변경한다(ProfileImageName profileImageName) {
        // given
        ChangeProfileRequest request = new ChangeProfileRequest(
                "행복한지구001",
                profileImageName.getKorean()
        );

        // when
        accountService.changeProfileInfo(1L, request);

        // then
        AccountResponse actual = accountService.readAccount(1L);

        assertAll(
                () -> assertThat(actual.nickname()).isEqualTo("행복한지구001"),
                () -> assertThat(actual.profileImage()).isEqualTo(profileImageName.getImageName())
        );
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 프로필 정보를 변경할 수 없다.")
    @NullAndEmptySource
    @Sql("classpath:sql/account/account.sql")
    void 프로필_이미지_경로가_비어_있으면_프로필_정보를_변경할_수_없다(String invalidProfileImageKoreanName) {
        // given
        ChangeProfileRequest request = new ChangeProfileRequest(
                "재빠른지구001",
                invalidProfileImageKoreanName
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeProfileInfo(1L, request))
                .isInstanceOf(InvalidProfileImageNameException.class)
                .hasMessageContaining("잘못된 프로필 이미지 이름");
    }

    @Test
    void 없거나_탈퇴한_회원의_ID라면_프로필_정보를_변경할_수_없다() {
        // given
        ChangeProfileRequest request = new ChangeProfileRequest(
                "재빠른지구001",
                "earth.png"
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeProfileInfo(-999L, request))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 회원_정보를_조회한다() {
        // when
        AccountResponse actual = accountService.readAccount(1L);

        // then
        assertAll(
                () -> assertThat(actual.jobGroupName()).isEqualTo("기타"),
                () -> assertThat(actual.companyName()).isEqualTo("스타트업"),
                () -> assertThat(actual.experienceName()).isEqualTo("1년 차 미만"),
                () -> assertThat(actual.nickname()).isEqualTo("재빠른지구001"),
                () -> assertThat(actual.profileImage()).isEqualTo("earth.png")
        );
    }

    @Test
    void 없거나_탈퇴한_회원의_ID라면_회원_정보를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> accountService.readAccount(-999L))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }
}
