package com.dnd.spaced.core.auth.application.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.auth.application.exception.NicknameMetadataNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SignUpServiceTest {

    @Autowired
    SignUpService signUpService;

    @Test
    void 닉네임_메타데이터가_정상적으로_초기화_되지_않았다면_회원가입을_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> signUpService.signUp(RegistrationId.KAKAO, "12345"))
                .isInstanceOf(NicknameMetadataNotFoundException.class)
                .hasMessage("닉네임 메타데이터가 정상적으로 초기화되지 않았습니다.");
    }

    @Test
    @Sql("classpath:sql/auth/nickname_metadata.sql")
    void 회원가입_한다() {
        // when
        Account actual = signUpService.signUp(RegistrationId.KAKAO, "12345");

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getSocialInfo().getSocialIdentifier()).isEqualTo("12345"),
                () -> assertThat(actual.getSocialInfo().getRegistrationId()).isEqualTo(RegistrationId.KAKAO)
        );
    }
}
