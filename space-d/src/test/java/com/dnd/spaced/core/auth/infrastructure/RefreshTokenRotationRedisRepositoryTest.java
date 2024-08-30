package com.dnd.spaced.core.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpRedis;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CleanUpRedis
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RefreshTokenRotationRedisRepositoryTest {

    @Autowired
    RefreshTokenRotationRedisRepository refreshTokenRotationRepository;

    @Test
    void save_메서드는_전달한_refreshToken을_저장한다() {
        // given
        String email = "email";
        String refreshToken = "refreshToken";

        // when & then
        assertDoesNotThrow(() -> refreshTokenRotationRepository.save(email, refreshToken));
    }

    @Test
    void findBy_메서드는_지정한_email에_해당하는_key가_없는_경우_빈_Optional을_반환한다() {
        // when
        Optional<String> actual = refreshTokenRotationRepository.findBy("email");

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findBy_메서드는_지정한_email에_해당하는_key가_있는_경우_해당_refreshToken을_반환한다() {
        // given
        String email = "email";
        String refreshToken = "refreshToken";

        refreshTokenRotationRepository.save(email, refreshToken);

        // when
        Optional<String> actual = refreshTokenRotationRepository.findBy(email);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get()).isEqualTo(refreshToken)
        );
    }
}
