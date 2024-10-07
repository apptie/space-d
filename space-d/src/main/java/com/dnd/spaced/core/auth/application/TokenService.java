package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import com.dnd.spaced.core.auth.application.exception.BlockedTokenException;
import com.dnd.spaced.core.auth.application.exception.ExpiredTokenException;
import com.dnd.spaced.core.auth.application.exception.RotationRefreshTokenMismatchException;
import com.dnd.spaced.core.auth.domain.PrivateClaims;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.domain.TokenType;
import com.dnd.spaced.core.auth.domain.repository.RefreshTokenRotationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

    private final TokenDecoder tokenDecoder;
    private final GenerateTokenService generateTokenService;
    private final BlacklistTokenService blacklistTokenService;
    private final RefreshTokenRotationRepository refreshTokenRotationRepository;

    @Transactional
    public TokenDto refreshToken(String refreshToken) {
        PrivateClaims privateClaims = convertTokenPrivateClaims(refreshToken);

        validateBlacklistToken(privateClaims);
        validateRotationRefreshToken(refreshToken, privateClaims);

        TokenDto tokenDto = generateTokenService.generate(privateClaims.accountId(), privateClaims.roleName());

        refreshTokenRotationRepository.save(privateClaims.accountId(), tokenDto.refreshToken());

        return tokenDto;
    }

    private PrivateClaims convertTokenPrivateClaims(String refreshToken) {
        return tokenDecoder.decode(TokenType.REFRESH, refreshToken)
                           .orElseThrow(
                                   () -> new ExpiredTokenException("Refresh Token이 만료되었습니다.")
                           );
    }

    private void validateBlacklistToken(PrivateClaims privateClaims) {
        if (blacklistTokenService.isBlockedToken(privateClaims)) {
            throw new BlockedTokenException("블랙리스트로 등록된 토큰입니다.");
        }
    }

    private void validateRotationRefreshToken(String refreshToken, PrivateClaims privateClaims) {
        refreshTokenRotationRepository.findBy(privateClaims.accountId())
                                      .ifPresent(
                                              rotateRefreshToken ->
                                                      validateRefreshToken(
                                                              refreshToken,
                                                              privateClaims,
                                                              rotateRefreshToken
                                                      )
                                      );
    }

    private void validateRefreshToken(String refreshToken, PrivateClaims privateClaims, String rotateRefreshToken) {
        if (!refreshToken.equals(rotateRefreshToken)) {
            blacklistTokenService.register(privateClaims.accountId());

            throw new RotationRefreshTokenMismatchException("기존 Refresh Token과 일치하지 않습니다.");
        }
    }
}
