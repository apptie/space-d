package com.dnd.spaced.core.auth.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpRedis;
import com.dnd.spaced.core.auth.domain.BlacklistToken;
import java.time.LocalDateTime;
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
class BlacklistTokenRedisRepositoryTest {

    @Autowired
    BlacklistTokenRedisRepository blacklistTokenRepository;

    @Test
    void 블랙리스트_토큰을_저장한다() {
        // given
        BlacklistToken blacklistToken = BlacklistToken.of(1L, LocalDateTime.now());

        // when & then
        assertDoesNotThrow(() -> blacklistTokenRepository.save(blacklistToken));
    }

    @Test
    void 블랙리스트로_등록되지_않은_회원_식별자로_블랙리스트_토큰을_조회한다() {
        // when
        Optional<BlacklistToken> actual = blacklistTokenRepository.findBy(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 블랙리스트로_등록된_회원_식별자로_블랙리스트_토큰을_조회한다() {
        // given
        Long accountId = 1L;
        BlacklistToken blacklistToken = BlacklistToken.of(accountId, LocalDateTime.now());

        blacklistTokenRepository.save(blacklistToken);

        // when
        Optional<BlacklistToken> actual = blacklistTokenRepository.findBy(accountId);

        // then
        assertThat(actual).isPresent()
                          .contains(blacklistToken);
    }
}
