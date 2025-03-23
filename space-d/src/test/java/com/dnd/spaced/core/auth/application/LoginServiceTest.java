package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.NicknameMetadata;
import com.dnd.spaced.core.account.domain.repository.NicknameMetadataRepository;
import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountInfoDto;
import com.dnd.spaced.core.auth.application.exception.NicknameMetadataNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@CleanUpDatabase
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Autowired
    NicknameMetadataRepository nicknameMetadataRepository;

    @Test
    void 회원가입하지_않은_회원이_로그인하면_회원_가입과_로그인_절차를_진행한다() {
        // given
        nicknameMetadataRepository.save(NicknameMetadata.from("재빠른지구"));

        // when
        LoggedInAccountInfoDto actual = loginService.login("kakao", "12345");

        // then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(actual.isSignUp()).isTrue()
        );
    }

    @Test
    void 회원가입한_회원이_로그인하면_로그인_절차를_진행한다() {
        // given
        nicknameMetadataRepository.save(NicknameMetadata.from("재빠른지구"));
        loginService.login("kakao", "12345");

        // when
        LoggedInAccountInfoDto actual = loginService.login("kakao", "12345");

        // then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(actual.isSignUp()).isFalse()
        );
    }

    @Test
    void 회원가입하지_않은_회원이_로그인하면서_회원_가입_절차에서_기존_닉네임과_동일한_닉네임을_부여받으면_닉네임_메타데이터를_갱신하고_로그인_절차를_진행한다() {
        // given
        nicknameMetadataRepository.save(NicknameMetadata.from("재빠른지구"));
        LoggedInAccountInfoDto loggedInAccountInfo1 = loginService.login("kakao", "12345");

        // when
        LoggedInAccountInfoDto loggedInAccountInfo2 = loginService.login("kakao", "54321");
        NicknameMetadata nicknameMetadata = nicknameMetadataRepository.findBy("재빠른지구")
                                                                      .get();

        // then
        assertAll(
                () -> assertThat(loggedInAccountInfo2.id()).isNotEqualTo(loggedInAccountInfo1.id()),
                () -> assertThat(loggedInAccountInfo2.roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(loggedInAccountInfo2.isSignUp()).isTrue(),
                () -> assertThat(nicknameMetadata.getCount()).isEqualTo(2L)
        );
    }

    @Test
    void 닉네임_메타데이터가_정상적으로_초기화되지_않았다면_로그인을_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> loginService.login("kakao", "54321"))
                .isInstanceOf(NicknameMetadataNotFoundException.class)
                .hasMessage("닉네임 메타데이터가 정상적으로 초기화되지 않았습니다.");
    }
}
