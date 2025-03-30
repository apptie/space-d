package com.dnd.spaced.global.auth.encryptor;

import com.dnd.spaced.global.auth.encryptor.exception.DecryptException;
import com.dnd.spaced.global.auth.encryptor.exception.EncryptException;
import com.dnd.spaced.global.auth.encryptor.exception.CipherPoolException;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Hex;

public class CbcEncryptor extends AbstractAesEncryptor implements Encryptor {

    private static final int IV_LENGTH = 16;
    private static final String CBC_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private final SecretKey secretKey;
    private final CipherPool cipherPool;

    public CbcEncryptor(String secretKey, String salt) {
        this.secretKey = createSecretKey(secretKey, AesBit.BIT_256, salt);
        this.cipherPool = new CipherPool(CBC_TRANSFORMATION);
    }

    @Override
    public String encrypt(String plainText) {
        Cipher cipher = null;

        try {
            cipher = cipherPool.borrowCipher();
            byte[] iv = new byte[IV_LENGTH];
            ThreadLocalRandom.current().nextBytes(iv);
            byte[] encrypted = encrypt(cipher, plainText, secretKey, new IvParameterSpec(iv));
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
            byte[] iv = Hex.decodeHex(cipherText.substring(0, IV_LENGTH * 2));
            byte[] decodedCipher = Hex.decodeHex(cipherText.substring(IV_LENGTH * 2));
            return decrypt(cipher, decodedCipher, secretKey, new IvParameterSpec(iv));
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
