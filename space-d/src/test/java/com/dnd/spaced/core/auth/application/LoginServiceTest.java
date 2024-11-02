package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.Role;
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

    @Test
    void 회원가입하지_않은_회원이_로그인하면_회원_가입과_로그인_절차를_진행한다() {
        // given
        String id = "email@email.com";

        // when
        LoggedInAccountInfoDto actual = loginService.login(id);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(id),
                () -> assertThat(actual.roleName()).isEqualTo(Role.ROLE_USER.name()),
                () -> assertThat(actual.isSignUp()).isTrue()
        );
    }

    @Test
    void 회원가입한_회원이_로그인하면_로그인_절차를_진행한다() {
        // given
        String id = "email@email.com";

        loginService.login(id);

        // when
        LoggedInAccountInfoDto actual = loginService.login(id);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(id),
                () -> assertThat(actual.roleName()).isEqualTo(Role.ROLE_USER.name()),
                () -> assertThat(actual.isSignUp()).isFalse()
        );
    }

    @Test
    void 회원가입하지_않은_회원이_로그인하면서_회원_가입_절차에서_기존_닉네임과_동일한_닉네임을_부여받으면_닉네임_메타데이터를_갱신하고_로그인_절차를_진행한다() {
        // given
        String id1 = "email1@email.com";
        String id2 = "email2@email.com";

        loginService.login(id1);

        // when
        LoggedInAccountInfoDto actual = loginService.login(id2);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(id2),
                () -> assertThat(actual.roleName()).isEqualTo(Role.ROLE_USER.name()),
                () -> assertThat(actual.isSignUp()).isTrue()
        );
    }
}
