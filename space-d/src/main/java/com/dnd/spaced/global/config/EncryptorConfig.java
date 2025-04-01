package com.dnd.spaced.global.config;

import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwsSignerFinder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwsVerifierFinder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwtDecoder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwtEncoder;
import com.dnd.spaced.global.auth.encryptor.Encryptor;
import com.dnd.spaced.global.auth.encryptor.CbcEncryptor;
import com.dnd.spaced.global.auth.encryptor.DelegatingEncryptor;
import com.dnd.spaced.global.auth.encryptor.GcmEncryptor;
import com.dnd.spaced.global.config.properties.AesProperties;
import com.dnd.spaced.global.config.properties.TokenProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.Map;
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
public class EncryptorConfig {

    private final Clock clock;
    private final AesProperties aesProperties;
    private final TokenProperties tokenProperties;

    @Bean
    public Encryptor aesEncryptor() {
        GcmEncryptor gcmAesEncryptor = new GcmEncryptor(aesProperties.secretKey(), aesProperties.salt());
        CbcEncryptor cbcAesEncryptor = new CbcEncryptor(aesProperties.secretKey(), aesProperties.salt());

        return new DelegatingEncryptor(
                "gcm",
                Map.of("gcm", gcmAesEncryptor, "cbc", cbcAesEncryptor),
                cbcAesEncryptor
        );
    }

    @Bean
    public TokenEncoder tokenEncoder(DirectEncrypter directEncrypter, JwsSignerFinder jwsSignerFinder) {
        return new JwtEncoder(directEncrypter, jwsSignerFinder, tokenProperties);
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
    public DirectDecrypter directDecrypter(SecretKey gcmAesSecretKey) throws KeyLengthException {
        return new DirectDecrypter(gcmAesSecretKey);
    }

    @Bean
    public DirectEncrypter directEncrypter(SecretKey gcmAesSecretKey) throws KeyLengthException {
        return new DirectEncrypter(gcmAesSecretKey);
    }

    @Bean
    public SecretKey gcmAesSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        return keyGenerator.generateKey();
    }
}
