package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.auth.domain.BlacklistToken;
import com.dnd.spaced.core.auth.domain.repository.BlacklistTokenRepository;
import com.dnd.spaced.core.auth.domain.PrivateClaims;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistTokenService {

    private final Clock clock;
    private final BlacklistTokenRepository blacklistTokenRepository;

    public boolean isBlockedToken(PrivateClaims privateClaims) {
        return blacklistTokenRepository.findBy(privateClaims.accountId())
                                       .map(blacklistToken -> blacklistToken.isBlacklistToken(privateClaims.issuedAt()))
                                       .orElse(Boolean.FALSE);
    }

    @Transactional
    public void register(String accountId) {
        BlacklistToken blacklistToken = new BlacklistToken(accountId, LocalDateTime.now(clock));

        blacklistTokenRepository.save(blacklistToken);
    }
}
