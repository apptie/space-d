package com.dnd.spaced.core.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpRedis;
import java.util.Optional;
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
class RefreshTokenRotationRedisRepositoryTest {

    @Autowired
    RefreshTokenRotationRedisRepository refreshTokenRotationRepository;

    @Test
    void 회원_식별자를_key로_refreshToken을_rotation으로_등록한다() {
        // when & then
        assertDoesNotThrow(() -> refreshTokenRotationRepository.save("user1@naver.com", "Bearer refreshToken"));
    }

    @Test
    void 등록하지_않은_회원_식별자로_refreshToken_rotation을_조회하면_빈_값을_반환한다() {
        // when
        Optional<String> actual = refreshTokenRotationRepository.findBy("user1@naver.com");

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 등록한_이메일로_refreshToken_rotation을_조회한다() {
        // given
        String id = "user1@naver.com";
        String refreshToken = "Bearer refreshToken";

        refreshTokenRotationRepository.save(id, refreshToken);

        // when
        Optional<String> actual = refreshTokenRotationRepository.findBy(id);

        // then
        assertThat(actual).isPresent()
                          .contains(refreshToken);
    }
}
