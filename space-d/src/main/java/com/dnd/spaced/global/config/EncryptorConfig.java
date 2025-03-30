package com.dnd.spaced.global.config;

import com.dnd.spaced.global.auth.encryptor.Encryptor;
import com.dnd.spaced.global.auth.encryptor.CbcEncryptor;
import com.dnd.spaced.global.auth.encryptor.DelegatingEncryptor;
import com.dnd.spaced.global.auth.encryptor.GcmEncryptor;
import com.dnd.spaced.global.config.properties.AesProperties;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AesProperties.class)
public class EncryptorConfig {

    private final AesProperties aesProperties;

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
}
