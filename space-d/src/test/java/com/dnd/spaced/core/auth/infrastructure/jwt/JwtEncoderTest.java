package com.dnd.spaced.core.auth.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.core.auth.domain.enums.TokenType;
import com.dnd.spaced.global.auth.encryptor.GcmEncryptor;
import com.dnd.spaced.global.config.properties.TokenProperties;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtEncoderTest {

    TokenProperties tokenProperties = new TokenProperties(
            "thisistoolargeaccesstokenkeyfordummykeydatafortest",
            "thisistoolargerefreshtokenkeyfordummykeydatafortest",
            "issuer",
            43200,
            259200,
            43200000L,
            259200000L
    );
    GcmEncryptor gcmAesEncryptor = new GcmEncryptor("secretKey", "salt");
    JwtEncoder jwtEncoder = new JwtEncoder(gcmAesEncryptor, tokenProperties);

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
