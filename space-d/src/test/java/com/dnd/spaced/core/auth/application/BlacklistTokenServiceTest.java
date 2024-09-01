package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.config.clean.annotation.CleanUpRedis;
import com.dnd.spaced.core.auth.domain.BlacklistToken;
import com.dnd.spaced.core.auth.domain.BlacklistTokenRepository;
import com.dnd.spaced.core.auth.domain.PrivateClaims;
import java.time.LocalDateTime;
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
class BlacklistTokenServiceTest {

    @Autowired
    BlacklistTokenService blacklistTokenService;

    @Autowired
    BlacklistTokenRepository blacklistTokenRepository;

    @Test
    void isBlockedToken_메서드는_블랙리스트로_등록되어_있지_않은_id의_PrivateClaims를_전달하면_fals를_반환한다() {
        // given
        PrivateClaims privateClaims = new PrivateClaims("id", "roleName", LocalDateTime.now());

        // when
        boolean actual = blacklistTokenService.isBlockedToken(privateClaims);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void isBlockedToken_메서드는_블랙리스트_등록_일자보다_isseudAt이_미래면_false를_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String id = "id";
        PrivateClaims privateClaims = new PrivateClaims(id, "roleName", now);

        blacklistTokenRepository.save(new BlacklistToken(id, now.minusDays(1L)));

        // when
        boolean actual = blacklistTokenService.isBlockedToken(privateClaims);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void isBlockedToken_메서드는_블랙리스트_등록_일자보다_isseudAt이_과거면_true를_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String id = "id";
        PrivateClaims privateClaims = new PrivateClaims(id, "roleName", now);

        blacklistTokenRepository.save(new BlacklistToken(id, now.plusDays(1L)));

        // when
        boolean actual = blacklistTokenService.isBlockedToken(privateClaims);

        // then
        assertThat(actual).isTrue();
    }
}
