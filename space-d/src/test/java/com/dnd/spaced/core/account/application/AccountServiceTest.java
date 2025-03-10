package com.dnd.spaced.core.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.application.dto.request.ChangeCareerInfoRequest;
import com.dnd.spaced.core.account.application.dto.request.ChangeProfileInfoRequest;
import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.core.account.application.exception.ForbiddenAccountException;
import com.dnd.spaced.core.account.application.helper.AccountServiceTestHelper;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@CleanUpDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountServiceTest extends AccountServiceTestHelper {

    @Autowired
    AccountService accountService;

    @Test
    void 지정한_회원을_탈퇴_처리한다() {
        // when
        accountService.withdrawal(account.getId());

        // then
        assertThat(accountRepository.findBy(account.getId())).isEmpty();
    }

    @Test
    void 없거나_이미_탈퇴한_회원의_ID라면_아니라면_탈퇴할_수_없다() {
        // when & then
        assertThatThrownBy(() -> accountService.withdrawal(-999L))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    @Test
    void 회원_경력_정보를_변경한다() {
        // given
        ChangeCareerInfoRequest request = new ChangeCareerInfoRequest(
                "개발자",
                "비공개",
                "1~2년 차"
        );

        // when
        accountService.changeCareerInfo(account.getId(), request);

        // then
        AccountResponse actual = accountService.readAccount(account.getId());

        assertAll(
                () -> assertThat(actual.jobGroupName()).isEqualTo("개발자"),
                () -> assertThat(actual.companyName()).isEqualTo("비공개"),
                () -> assertThat(actual.experienceName()).isEqualTo("1~2년 차")
        );
    }

    @ParameterizedTest(name = "직군 이름이 {0}일 때 경력 정보를 변경할 수 없다.")
    @NullAndEmptySource
    void 유효한_직군_이름이_아니라면_경력_정보를_변경할_수_없다(String invalidJobGroupName) {
        // given
        ChangeCareerInfoRequest request = new ChangeCareerInfoRequest(
                invalidJobGroupName,
                "비공개",
                "1~2년 차"
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeCareerInfo(account.getId(), request))
                .isInstanceOf(InvalidJobGroupException.class)
                .hasMessageContaining("잘못된 직군 이름");
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 경력 정보를 변경할 수 없다.")
    @NullAndEmptySource
    void 유효한_회사명이_아니라면_경력_정보를_변경할_수_없다(String invalidCompanyName) {
        // given
        ChangeCareerInfoRequest request = new ChangeCareerInfoRequest(
                "개발자",
                invalidCompanyName,
                "1~2년 차"
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeCareerInfo(account.getId(), request))
                .isInstanceOf(InvalidCompanyException.class)
                .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "경력이 {0}일 때 경력 정보를 변경할 수 없다.")
    @NullAndEmptySource
    void 유효한_경력이_아니라면_경력_정보를_변경할_수_없다(String invalidExperienceName) {
        // given
        ChangeCareerInfoRequest request = new ChangeCareerInfoRequest(
                "개발자",
                "비공개",
                invalidExperienceName
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeCareerInfo(account.getId(), request))
                .isInstanceOf(InvalidExperienceException.class)
                .hasMessageContaining("잘못된 경력");
    }

    @Test
    void 없거나_탈퇴한_회원의_ID라면_경력_정보를_변경할_수_없다() {
        // given
        ChangeCareerInfoRequest request = new ChangeCareerInfoRequest(
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
    void 회원_프로필_정보를_변경한다(ProfileImageName profileImageName) {
        // given
        ChangeProfileInfoRequest request = new ChangeProfileInfoRequest(
                "행복한지구001",
                profileImageName.getKorean()
        );

        // when
        accountService.changeProfileInfo(account.getId(), request);

        // then
        AccountResponse actual = accountService.readAccount(account.getId());

        assertAll(
                () -> assertThat(actual.nickname()).isEqualTo("행복한지구001"),
                () -> assertThat(actual.profileImage()).isEqualTo(profileImageName.getImageName())
        );
    }

    @ParameterizedTest(name = "프로필 이미지가 {0}일 때 프로필 정보를 변경할 수 없다.")
    @NullAndEmptySource
    void 프로필_이미지_경로가_비어_있으면_프로필_정보를_변경할_수_없다(String invalidProfileImageKoreanName) {
        // given
        ChangeProfileInfoRequest request = new ChangeProfileInfoRequest(
                "재빠른지구001",
                invalidProfileImageKoreanName
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeProfileInfo(account.getId(), request))
                .isInstanceOf(InvalidProfileImageNameException.class)
                .hasMessageContaining("잘못된 프로필 이미지 이름");
    }

    @Test
    void 없거나_탈퇴한_회원의_ID라면_프로필_정보를_변경할_수_없다() {
        // given
        ChangeProfileInfoRequest request = new ChangeProfileInfoRequest(
                "재빠른지구001",
                "earth.png"
        );

        // when & then
        assertThatThrownBy(() -> accountService.changeProfileInfo(-999L, request))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }

    @Test
    void 회원_정보를_조회한다() {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        account.changeCareerInfo(
                "개발자",
                "비공개",
                "1~2년 차"
        );
        accountRepository.save(account);

        // when
        AccountResponse actual = accountService.readAccount(account.getId());

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
    void 없거나_탈퇴한_회원의_ID라면_회원_정보를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> accountService.readAccount(-999L))
                .isInstanceOf(ForbiddenAccountException.class)
                .hasMessage("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다.");
    }
}
