package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import com.dnd.spaced.core.auth.domain.enums.TokenScheme;
import com.dnd.spaced.core.auth.domain.repository.RefreshTokenRotationRepository;
import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.domain.enums.TokenType;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerateTokenService {

    private final Clock clock;
    private final TokenEncoder tokenEncoder;
    private final RefreshTokenRotationRepository refreshTokenRotationRepository;

    @Transactional
    public TokenDto generate(Long accountId, String roleName) {
        String accessToken = tokenEncoder.encode(LocalDateTime.now(clock), TokenType.ACCESS, accountId, roleName);
        String refreshToken = tokenEncoder.encode(LocalDateTime.now(clock), TokenType.REFRESH, accountId, roleName);

        refreshTokenRotationRepository.save(accountId, refreshToken);

        return new TokenDto(accessToken, refreshToken, TokenScheme.BEARER.name());
    }
}
