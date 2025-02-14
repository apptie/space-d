package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.NicknameMetadata;
import com.dnd.spaced.core.account.domain.repository.NicknameMetadataRepository;
import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountInfoDto;
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
        String id = "user1@naver.com";

        // when
        LoggedInAccountInfoDto actual = loginService.login(id);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(id),
                () -> assertThat(actual.roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(actual.isSignUp()).isTrue()
        );
    }

    @Test
    void 회원가입한_회원이_로그인하면_로그인_절차를_진행한다() {
        // given
        String id = "user1@naver.com";

        loginService.login(id);

        // when
        LoggedInAccountInfoDto actual = loginService.login(id);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(id),
                () -> assertThat(actual.roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(actual.isSignUp()).isFalse()
        );
    }

    @Test
    void 회원가입하지_않은_회원이_로그인하면서_회원_가입_절차에서_기존_닉네임과_동일한_닉네임을_부여받으면_닉네임_메타데이터를_갱신하고_로그인_절차를_진행한다() {
        // given
        String id1 = "user1@naver.com";
        String id2 = "user2@naver.com";

        loginService.login(id1);

        // when
        LoggedInAccountInfoDto loggedInAccountInfo = loginService.login(id2);
        NicknameMetadata nicknameMetadata = nicknameMetadataRepository.findBy("재빠른지구")
                                                                      .get();

        // then
        assertAll(
                () -> assertThat(loggedInAccountInfo.id()).isEqualTo(id2),
                () -> assertThat(loggedInAccountInfo.roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(loggedInAccountInfo.isSignUp()).isTrue(),
                () -> assertThat(nicknameMetadata.getCount()).isEqualTo(2)
        );
    }
}
