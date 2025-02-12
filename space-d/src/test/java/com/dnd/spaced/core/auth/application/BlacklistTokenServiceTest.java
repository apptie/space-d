package com.dnd.spaced.core.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.config.clean.annotation.CleanUpRedis;
import com.dnd.spaced.core.account.domain.Role;
import com.dnd.spaced.core.auth.domain.BlacklistToken;
import com.dnd.spaced.core.auth.domain.repository.BlacklistTokenRepository;
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
    void 토큰_블랙리스트에_등록되지_않은_회원의_토큰은_유효한_토큰이다() {
        // given
        LocalDateTime tokenIssuedAt = LocalDateTime.of(2022, 2, 2, 13, 13);
        PrivateClaims privateClaims = new PrivateClaims(
                "user1@naver.com",
                "ROLE_USER",
                tokenIssuedAt
        );

        // when
        boolean actual = blacklistTokenService.isBlockedToken(privateClaims);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 토큰_블랙리스트에_회원이_등록된_날짜보다_토큰의_생성_일자가_미래라면_유효한_토큰이다() {
        // given
        LocalDateTime tokenIssuedAt = LocalDateTime.of(2022, 2, 2, 13, 13);
        LocalDateTime registerBlacklistTokenAt = LocalDateTime.of(2022, 2, 1, 13, 13);
        String accountId = "user1@naver.com";
        PrivateClaims privateClaims = new PrivateClaims(accountId, "ROLE_USER", tokenIssuedAt);

        blacklistTokenRepository.save(new BlacklistToken(accountId, registerBlacklistTokenAt));

        // when
        boolean actual = blacklistTokenService.isBlockedToken(privateClaims);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 토큰_블랙리스트에_회원이_등록된_날짜보다_토큰의_생성_일자가_과거라면_차단된_토큰이다() {
        // given
        LocalDateTime tokenIssuedAt = LocalDateTime.of(2022, 2, 2, 13, 13);
        LocalDateTime registerBlacklistTokenAt = LocalDateTime.of(2022, 2, 3, 13, 13);
        String accountId = "user1@naver.com";
        PrivateClaims privateClaims = new PrivateClaims(accountId, Role.ROLE_USER.name(), tokenIssuedAt);

        blacklistTokenRepository.save(new BlacklistToken(accountId, registerBlacklistTokenAt));

        // when
        boolean actual = blacklistTokenService.isBlockedToken(privateClaims);

        // then
        assertThat(actual).isTrue();
    }
}
