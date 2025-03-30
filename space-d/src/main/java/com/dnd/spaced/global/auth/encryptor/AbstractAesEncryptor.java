package com.dnd.spaced.global.auth.encryptor;

import com.dnd.spaced.global.auth.encryptor.exception.CreateSecretKeyException;
import io.netty.handler.codec.DecoderException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class AbstractAesEncryptor {

    private static final String ALGORITHM = "AES";
    private static final int ITERATOR_COUNT = 65536;
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";

    protected byte[] encrypt(
            Cipher cipher,
            String plainText,
            SecretKey secretKey,
            AlgorithmParameterSpec parameterSpec
    ) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, DecoderException {
        Cipher targetCipher = initEncryptCipher(cipher, secretKey, parameterSpec);

        return targetCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    protected String decrypt(
            Cipher cipher,
            byte[] cipherBytes,
            SecretKey secretKey,
            AlgorithmParameterSpec parameterSpec
    ) throws InvalidKeyException, InvalidAlgorithmParameterException, DecoderException, IllegalBlockSizeException, BadPaddingException {
        Cipher targetCipher = initDecryptCipher(cipher, secretKey, parameterSpec);

        return new String(targetCipher.doFinal(cipherBytes), StandardCharsets.UTF_8);
    }

    protected SecretKey createSecretKey(String secretKey, AesBit aesBit, String salt) {
        validateSecretKey(secretKey);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
            int keyLength = aesBit.getBitLength();
            byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), saltBytes, ITERATOR_COUNT, keyLength);
            SecretKey tmp = factory.generateSecret(spec);

            return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
        } catch (Exception e) {
            throw new CreateSecretKeyException(e);
        }
    }

    private void validateSecretKey(String secretKey) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new CreateSecretKeyException();
        }
    }

    private Cipher initEncryptCipher(
            Cipher cipher,
            SecretKey secretKey,
            AlgorithmParameterSpec parameterSpec
    )
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (parameterSpec == null) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher;
        }
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        return cipher;
    }

    private Cipher initDecryptCipher(
            Cipher cipher,
            SecretKey secretKey,
            AlgorithmParameterSpec parameterSpec
    )
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (parameterSpec == null) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher;
        }
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        return cipher;
    }
}
