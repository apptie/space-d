package com.dnd.spaced.global.auth.encryptor;

import com.dnd.spaced.global.auth.encryptor.exception.DecryptException;
import com.dnd.spaced.global.auth.encryptor.exception.EncryptException;
import com.dnd.spaced.global.auth.encryptor.exception.CipherPoolException;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.apache.commons.codec.binary.Hex;

public class GcmEncryptor extends AbstractAesEncryptor implements Encryptor {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_BIT_LENGTH = 128;
    private static final String GCM_TRANSFORMATION = "AES/GCM/NoPadding";

    private final SecretKey secretKey;
    private final CipherPool cipherPool;

    public GcmEncryptor(String secretKey, String salt) {
        this.secretKey = createSecretKey(secretKey, AesBit.BIT_256, salt);
        this.cipherPool = new CipherPool(GCM_TRANSFORMATION);
    }

    @Override
    public String encrypt(String plainText) {
        Cipher cipher = null;

        try {
            cipher = cipherPool.borrowCipher();
            byte[] iv = new byte[GCM_IV_LENGTH];
            ThreadLocalRandom.current().nextBytes(iv);
            byte[] encrypted = encrypt(cipher, plainText, secretKey, new GCMParameterSpec(GCM_BIT_LENGTH, iv));
            return Hex.encodeHexString(iv) + Hex.encodeHexString(encrypted);
        } catch (Exception e) {
            throw new EncryptException(e);
        } finally {
            returnCipher(cipher);
        }
    }

    @Override
    public String decrypt(String cipherText) {
        Cipher cipher = null;

        try {
            cipher = cipherPool.borrowCipher();
            byte[] iv = Hex.decodeHex(cipherText.substring(0, GCM_IV_LENGTH * 2));
            byte[] decodedCipher = Hex.decodeHex(cipherText.substring(GCM_IV_LENGTH * 2));
            return decrypt(cipher, decodedCipher, secretKey, new GCMParameterSpec(GCM_BIT_LENGTH, iv));
        } catch (Exception e) {
            throw new DecryptException(e);
        } finally {
            returnCipher(cipher);
        }
    }

    private void returnCipher(Cipher cipher) {
        try {
            cipherPool.returnCipher(cipher);
        } catch (Exception e) {
            throw new CipherPoolException(e);
        }
    }
}
