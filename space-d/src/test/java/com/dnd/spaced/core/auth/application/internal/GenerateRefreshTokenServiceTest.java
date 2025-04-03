package com.dnd.spaced.core.auth.application.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GenerateRefreshTokenServiceTest {

    @Autowired
    GenerateTokenService generateTokenService;

    @Test
    void accessToken과_refreshToken을_생성한다() {
        // when
        TokenDto actual = generateTokenService.generate(1L, "ROLE_USER");

        // then
        assertAll(
                () -> assertThat(actual.accessToken()).isNotBlank(),
                () -> assertThat(actual.refreshToken()).isNotBlank()
        );
    }
}
