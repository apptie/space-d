package com.dnd.spaced.core.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.core.auth.domain.TokenType;
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
    JwtEncoder jwtEncoder = new JwtEncoder(tokenProperties);

    @ParameterizedTest
    @EnumSource(value = TokenType.class)
    void encode_메서드는_유효한_claims_데이터를_전달하면_token을_반환한다(TokenType tokenType) {
        // when
        String actual = jwtEncoder.encode(LocalDateTime.now(), tokenType, "email", "roleName");

        // then
        assertThat(actual).isNotBlank();
    }
}
