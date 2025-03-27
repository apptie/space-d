package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountInfoDto;
import com.dnd.spaced.core.auth.application.exception.NicknameMetadataNotFoundException;
import com.dnd.spaced.core.skill.application.event.dto.InitializedAccountEvent;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Autowired
    ApplicationEvents events;

    @Test
    @Sql(scripts = {"classpath:sql/cleanup.sql", "classpath:sql/auth/nickname_metadata.sql"})
    void 회원가입하지_않은_회원이_로그인하면_회원_가입과_로그인_절차를_진행한다() {
        // when
        LoggedInAccountInfoDto actual = loginService.login("kakao", "12345");

        // then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(actual.isSignUp()).isTrue(),
                () -> assertThat(events.stream(InitializedAccountEvent.class).count()).isOne()
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/auth/nickname_metadata.sql",
            "classpath:sql/auth/account.sql"
    })
    void 회원가입한_회원이_로그인하면_로그인_절차를_진행한다() {
        // given
        // when
        LoggedInAccountInfoDto actual = loginService.login("kakao", "12345");

        // then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.roleName()).isEqualTo("ROLE_USER"),
                () -> assertThat(actual.isSignUp()).isFalse(),
                () -> assertThat(events.stream(InitializedAccountEvent.class).count()).isZero()
        );
    }

    @Test
    @Sql("classpath:sql/cleanup.sql")
    void 닉네임_메타데이터가_정상적으로_초기화되지_않았다면_로그인을_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> loginService.login("kakao", "54321"))
                .isInstanceOf(NicknameMetadataNotFoundException.class)
                .hasMessage("닉네임 메타데이터가 정상적으로 초기화되지 않았습니다.");
    }
}
