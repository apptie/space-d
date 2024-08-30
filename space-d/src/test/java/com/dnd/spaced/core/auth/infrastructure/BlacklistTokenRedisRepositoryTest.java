package com.dnd.spaced.core.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpRedis;
import com.dnd.spaced.core.auth.domain.BlacklistToken;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
class BlacklistTokenRedisRepositoryTest {

    @Autowired
    BlacklistTokenRedisRepository blacklistTokenRepository;

    @Test
    void save_메서드는_전달한_blacklistToken을_저장한다() {
        // given
        String email = "email";
        LocalDateTime registeredAt = LocalDateTime.now();
        BlacklistToken blacklistToken = new BlacklistToken(email, registeredAt);

        // when & then
        assertDoesNotThrow(() -> blacklistTokenRepository.save(blacklistToken));
    }

    @Test
    void findBy_메서드는_지정한_email에_해당하는_key가_없는_경우_빈_Optional을_반환한다() {
        // when
        Optional<BlacklistToken> actual = blacklistTokenRepository.findBy("email");

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findBy_메서드는_지정한_email에_해당하는_key가_있는_경우_해당_blacklistToken을_반환한다() {
        // given
        String email = "email";
        LocalDateTime registeredAt = LocalDateTime.now();
        BlacklistToken blacklistToken = new BlacklistToken(email, registeredAt);

        blacklistTokenRepository.save(blacklistToken);

        // when
        Optional<BlacklistToken> actual = blacklistTokenRepository.findBy(email);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getEmail()).isEqualTo(email),
                () -> assertThat(actual.get().getRegisteredAt()).isEqualTo(registeredAt.truncatedTo(ChronoUnit.SECONDS))
        );
    }
}
