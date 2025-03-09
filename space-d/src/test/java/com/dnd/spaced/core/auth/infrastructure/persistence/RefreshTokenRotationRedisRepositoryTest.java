package com.dnd.spaced.core.auth.infrastructure.persistence;

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
    void 회원_ID를_key로_refreshToken을_rotation으로_등록한다() {
        // when & then
        assertDoesNotThrow(() -> refreshTokenRotationRepository.save(1L, "Bearer refreshToken"));
    }

    @Test
    void 등록하지_않은_회원_ID로_refreshToken_rotation을_조회하면_빈_값을_반환한다() {
        // when
        Optional<String> actual = refreshTokenRotationRepository.findBy(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 등록한_이메일로_refreshToken_rotation을_조회한다() {
        // given
        Long accountId = 1L;
        String refreshToken = "Bearer refreshToken";

        refreshTokenRotationRepository.save(accountId, "Bearer refreshToken");

        // when
        Optional<String> actual = refreshTokenRotationRepository.findBy(accountId);

        // then
        assertThat(actual).isPresent()
                          .contains(refreshToken);
    }
}
