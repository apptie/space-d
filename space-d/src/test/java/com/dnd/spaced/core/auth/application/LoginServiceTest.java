package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.Role;
import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountInfoDto;
import com.dnd.spaced.global.config.properties.NicknameProperties;
import java.util.Collections;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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
    void login_메서드는_회원가입하지_않은_id를_전달하면_회원가입_이후_회원_정보를_반환한다() {
        // given
        String id = "id";

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
    void login_메서드는_회원가입한_id를_전달하면_회원_정보를_반환한다() {
        // given
        String id = "id";

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
    void login_메서드는_회원가입하지_않은_id를_전달하면_닉네임_메타데이터를_갱신_및_회원가입_이후_회원_정보를_반환한다() {
        // given
        String id1 = "id1";
        String id2 = "id2";

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

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public NicknameProperties nicknameProperties() {
            return new TestNicknameProperties();
        }
    }

    static class TestNicknameProperties extends NicknameProperties {

        public TestNicknameProperties() {
            super(Collections.emptyList(), Collections.emptyList(), "%03d");
        }

        @Override
        public String generate() {
            return "재빠른지구";
        }
    }
}
