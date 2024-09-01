package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpRedis;
import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@CleanUpRedis
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GenerateTokenServiceTest {

    @Autowired
    GenerateTokenService generateTokenService;

    @Test
    void generate_메서드는_id와_roleName을_전달하면_accessToken과_refreshToken을_초기화하고_반환한다() {
        // given
        String id = "id";
        String roleName = "roleName";

        // when
        TokenDto actual = generateTokenService.generate(id, roleName);

        // then
        assertAll(
                () -> assertThat(actual.accessToken()).startsWith("Bearer "),
                () -> assertThat(actual.refreshToken()).startsWith("Bearer ")
        );
    }
}
