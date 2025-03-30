package com.dnd.spaced.global.auth.encryptor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DelegatingEncryptorTest {

    @Test
    void 성공_테스트() {
        // given
        CbcEncryptor cbcAesEncryptor = new CbcEncryptor("thisissecretkey", "salt");
        GcmEncryptor gcmAesEncryptor = new GcmEncryptor("thisissecretkey", "salt");
        DelegatingEncryptor delegatingAesEncryptor = new DelegatingEncryptor(
                "gcm",
                Map.of("gcm", gcmAesEncryptor, "cbc", cbcAesEncryptor),
                cbcAesEncryptor
        );

        // when & then
        Set<String> encryptSet = new HashSet<>();

        for (int i = 0; i < 20; i++) {
            String encrypt = delegatingAesEncryptor.encrypt("999");
            encryptSet.add(encrypt);
        }

        assertThat(encryptSet).hasSize(20);

        for (String cipherText : encryptSet) {
            assertThat(delegatingAesEncryptor.decrypt(cipherText)).isEqualTo("999");
        }
    }
}
