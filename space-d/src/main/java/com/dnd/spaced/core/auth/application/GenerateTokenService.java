package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import com.dnd.spaced.core.auth.domain.repository.RefreshTokenRotationRepository;
import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.domain.TokenType;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenerateTokenService {

    private final Clock clock;
    private final TokenEncoder tokenEncoder;
    private final RefreshTokenRotationRepository refreshTokenRotationRepository;

    @Transactional
    public TokenDto generate(String id, String roleName) {
        String accessToken = tokenEncoder.encode(LocalDateTime.now(clock), TokenType.ACCESS, id, roleName);
        String refreshToken = tokenEncoder.encode(LocalDateTime.now(clock), TokenType.REFRESH, id, roleName);

        refreshTokenRotationRepository.save(id, refreshToken);

        return new TokenDto(accessToken, refreshToken);
    }
}
