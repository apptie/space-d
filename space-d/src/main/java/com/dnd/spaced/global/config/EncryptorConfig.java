package com.dnd.spaced.global.config;

import com.dnd.spaced.core.auth.domain.TokenEncoder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwsSignerFinder;
import com.dnd.spaced.core.auth.infrastructure.jwt.JwtEncoder;
import com.dnd.spaced.global.auth.encryptor.Encryptor;
import com.dnd.spaced.global.auth.encryptor.CbcEncryptor;
import com.dnd.spaced.global.auth.encryptor.DelegatingEncryptor;
import com.dnd.spaced.global.auth.encryptor.GcmEncryptor;
import com.dnd.spaced.global.config.properties.AesProperties;
import com.dnd.spaced.global.config.properties.TokenProperties;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
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
    public JwsSignerFinder jwsSignerFinder() throws KeyLengthException {
        byte[] accessTokenKeyBytes = tokenProperties.accessKey().getBytes(StandardCharsets.UTF_8);
        SecretKey accessTokenSecretKey = new SecretKeySpec(accessTokenKeyBytes, "HmacSHA256");
        MACSigner accessTokenSigner = new MACSigner(accessTokenSecretKey);

        byte[] refreshTokenKeyBytes = tokenProperties.accessKey().getBytes(StandardCharsets.UTF_8);
        SecretKey refreshTokenSecretKey = new SecretKeySpec(refreshTokenKeyBytes, "HmacSHA256");
        MACSigner refreshTokenSigner = new MACSigner(refreshTokenSecretKey);

        return new JwsSignerFinder(accessTokenSigner, refreshTokenSigner);
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
