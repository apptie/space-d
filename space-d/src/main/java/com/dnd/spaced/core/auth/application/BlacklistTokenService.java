package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.auth.domain.BlacklistTokenRepository;
import com.dnd.spaced.core.auth.domain.PrivateClaims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistTokenService {

    private final BlacklistTokenRepository blacklistTokenRepository;

    public boolean isBlockedToken(PrivateClaims privateClaims) {
        return blacklistTokenRepository.findBy(privateClaims.id())
                                       .map(blacklistToken -> blacklistToken.isBlacklistToken(privateClaims.issuedAt()))
                                       .orElse(Boolean.FALSE);
    }
}
