package com.dnd.spaced.core.auth.infrastructure.jwt;

import com.dnd.spaced.core.auth.domain.PrivateClaims;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.dnd.spaced.core.auth.infrastructure.jwt.exception.FailedDecodeTokenException;
import com.dnd.spaced.core.auth.infrastructure.jwt.exception.InvalidTokenException;
import com.dnd.spaced.global.config.properties.TokenProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtDecoder implements TokenDecoder {

    private static final String CLAIM_ID = "id";
    private static final String CLAIM_ROLE = "role";

    private final Clock clock;
    private final JWEDecrypter jweDecrypter;
    private final JwsVerifierFinder jwsVerifierFinder;
    private final TokenProperties tokenProperties;

    @Override
    public Optional<PrivateClaims> decode(TokenType tokenType, String token) {
        validateToken(token);

        return this.parse(tokenType, token)
                   .map(this::convert);
    }

    private void validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("토큰이 존재하지 않거나 길이가 부족합니다.");
        }
    }

    private Optional<JWTClaimsSet> parse(TokenType tokenType, String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);

            jweObject.decrypt(jweDecrypter);

            SignedJWT signedJwt = jweObject.getPayload()
                                           .toSignedJWT();
            JWSVerifier jwsVerifier = jwsVerifierFinder.findByTokenType(tokenType);

            validateSign(signedJwt, jwsVerifier);

            JWTClaimsSet claims = signedJwt.getJWTClaimsSet();

            validateIssuer(claims.getIssuer());
            if (isExpiredToken(claims.getExpirationTime())) {
                return Optional.empty();
            }

            return Optional.of(claims);
        } catch (JOSEException e) {
            throw new FailedDecodeTokenException("토큰 디코딩에 실패했습니다", e);
        } catch (ParseException e) {
            throw new InvalidTokenException("유효한 토큰이 아닙니다.", e);
        }
    }

    private void validateSign(SignedJWT signedJwt, JWSVerifier jwsVerifier) throws JOSEException {
        if (!signedJwt.verify(jwsVerifier)) {
            throw new InvalidTokenException("위변조된 토큰입니다.");
        }
    }

    private boolean isExpiredToken(Date expirationTime) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime expirationDate = LocalDateTime.ofInstant(expirationTime.toInstant(), ZoneId.systemDefault());

        return expirationDate.isBefore(now);
    }

    private void validateIssuer(String issuer) {
        if (!tokenProperties.issuer().equals(issuer)) {
            throw new InvalidTokenException("서비스에서 발급한 토큰이 아닙니다.");
        }
    }

    private PrivateClaims convert(JWTClaimsSet claims) {
        Date issueTime = claims.getIssueTime();

        try {
            return new PrivateClaims(
                    claims.getLongClaim(CLAIM_ID),
                    claims.getStringClaim(CLAIM_ROLE),
                    LocalDateTime.ofInstant(issueTime.toInstant(), ZoneId.systemDefault())
            );
        } catch (ParseException e) {
            throw new InvalidTokenException("유효한 형식의 토큰이 아닙니다.");
        }
    }
}
