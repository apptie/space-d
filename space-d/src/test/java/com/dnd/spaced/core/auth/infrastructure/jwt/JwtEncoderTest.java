package com.dnd.spaced.core.auth.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.dnd.spaced.global.auth.encryptor.GcmEncryptor;
import com.dnd.spaced.global.config.properties.TokenProperties;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtEncoderTest {

    TokenProperties tokenProperties = new TokenProperties(
            "thisistoolargeaccesstokenkeyfordummykeydatafortestthisistoolargeaccesstokenkeyfordummykeydatafortestthisistoolargeaccesstokenkeyfordummykeydatafortestthisistoolargeaccesstokenkeyfordummykeydatafortestthisistoolargeaccesstokenkeyfordummykeydatafortestthisistoolargeaccesstokenkeyfordummykeydatafortest",
            "thisistoolargerefreshtokenkeyfordummykeydatafortestthisistoolargerefreshtokenkeyfordummykeydatafortestthisistoolargerefreshtokenkeyfordummykeydatafortestthisistoolargerefreshtokenkeyfordummykeydatafortestthisistoolargerefreshtokenkeyfordummykeydatafortestthisistoolargerefreshtokenkeyfordummykeydatafortest",
            "issuer",
            43200,
            259200,
            43200000L,
            259200000L
    );

    JwtEncoder jwtEncoder;

    @BeforeEach
    void beforeEach() throws NoSuchAlgorithmException, KeyLengthException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        DirectEncrypter directEncrypter = new DirectEncrypter(secretKey);
        byte[] accessTokenKeyBytes = tokenProperties.accessKey().getBytes(StandardCharsets.UTF_8);
        SecretKey accessTokenSecretKey = new SecretKeySpec(accessTokenKeyBytes, "HmacSHA256");
        MACSigner accessTokenSigner = new MACSigner(accessTokenSecretKey);
        byte[] refreshTokenKeyBytes = tokenProperties.accessKey().getBytes(StandardCharsets.UTF_8);
        SecretKey refreshTokenSecretKey = new SecretKeySpec(refreshTokenKeyBytes, "HmacSHA256");
        MACSigner refreshTokenSigner = new MACSigner(refreshTokenSecretKey);
        JwsSignerFinder jwsSignerFinder = new JwsSignerFinder(accessTokenSigner, refreshTokenSigner);

        jwtEncoder = new JwtEncoder(directEncrypter, jwsSignerFinder, tokenProperties);
    }

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void 토큰을_인코딩한다(TokenType tokenType) {
        // when
        String actual = jwtEncoder.encode(
                LocalDateTime.now(),
                tokenType,
                1L,
                "ROLE_USER"
        );

        // then
        assertThat(actual).isNotBlank();
    }
}
