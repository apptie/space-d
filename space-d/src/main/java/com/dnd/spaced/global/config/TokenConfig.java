package com.dnd.spaced.global.config;

import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwsSignerFinder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwsVerifierFinder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwtDecoder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwtEncoder;
import com.dnd.spaced.global.config.properties.AesProperties;
import com.dnd.spaced.global.config.properties.TokenProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.AESDecrypter;
import com.nimbusds.jose.crypto.AESEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AesProperties.class)
public class TokenConfig {

    private final Clock clock;
    private final TokenProperties tokenProperties;

    @Bean
    public TokenEncoder tokenEncoder(JWEEncrypter jweEncrypter, JwsSignerFinder jwsSignerFinder) {
        return new JwtEncoder(jweEncrypter, jwsSignerFinder, tokenProperties);
    }

    @Bean
    public TokenDecoder tokenDecoder(JWEDecrypter jweDecrypter, JwsVerifierFinder jwsVerifierFinder) {
        return new JwtDecoder(clock, jweDecrypter, jwsVerifierFinder, tokenProperties);
    }

    @Bean
    public JwsVerifierFinder jwsVerifierFinder(
            SecretKey accessTokenSecretKey,
            SecretKey refreshTokenSecretKey
    ) throws JOSEException {
        JWSVerifier accessTokenJwsVerifier = new MACVerifier(accessTokenSecretKey);
        JWSVerifier refreshTokenJwsVerifier = new MACVerifier(refreshTokenSecretKey);

        return new JwsVerifierFinder(accessTokenJwsVerifier, refreshTokenJwsVerifier);
    }

    @Bean
    public JwsSignerFinder jwsSignerFinder(
            SecretKey accessTokenSecretKey,
            SecretKey refreshTokenSecretKey
    ) throws KeyLengthException {
        MACSigner accessTokenSigner = new MACSigner(accessTokenSecretKey);
        MACSigner refreshTokenSigner = new MACSigner(refreshTokenSecretKey);

        return new JwsSignerFinder(accessTokenSigner, refreshTokenSigner);
    }

    @Bean
    public SecretKey accessTokenSecretKey() {
        byte[] accessTokenKeyBytes = tokenProperties.accessKey().getBytes(StandardCharsets.UTF_8);

        return new SecretKeySpec(accessTokenKeyBytes, "HmacSHA256");
    }

    @Bean
    public SecretKey refreshTokenSecretKey() {
        byte[] refreshTokenKeyBytes = tokenProperties.refreshKey().getBytes(StandardCharsets.UTF_8);

        return new SecretKeySpec(refreshTokenKeyBytes, "HmacSHA256");
    }

    @Bean
    public JWEDecrypter jweDecrypter(SecretKey gcmAesSecretKey) throws KeyLengthException {
        return new AESDecrypter(gcmAesSecretKey);
    }

    @Bean
    public JWEEncrypter jweEncrypter(SecretKey gcmAesSecretKey) throws KeyLengthException {
        return new AESEncrypter(gcmAesSecretKey);
    }

    @Bean
    public SecretKey gcmAesSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        return keyGenerator.generateKey();
    }
}
