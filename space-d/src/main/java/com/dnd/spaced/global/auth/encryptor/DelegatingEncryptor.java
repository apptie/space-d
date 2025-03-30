package com.dnd.spaced.global.auth.encryptor;

import java.util.Map;

public class DelegatingEncryptor implements Encryptor {

    private static final String PREFIX = "{";
    private static final String SUFFIX = "}";

    private final String idToEncrypt;
    private final Map<String, Encryptor> idToEncryptor;
    private final Encryptor defaultEncryptor;
    private final Encryptor fallback;

    public DelegatingEncryptor(
            String idToEncrypt,
            Map<String, Encryptor> idToEncryptor,
            Encryptor fallback
    ) {
        this.idToEncrypt = idToEncrypt;
        this.idToEncryptor = idToEncryptor;
        this.fallback = fallback;
        this.defaultEncryptor = idToEncryptor.get(idToEncrypt);
    }

    @Override
    public String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }

        return PREFIX + idToEncrypt + SUFFIX + defaultEncryptor.encrypt(plainText);
    }

    @Override
    public String decrypt(String cipherText) {
        String id = extractId(cipherText);
        if (id == null) {
            return fallback.decrypt(cipherText);
        }
        Encryptor encryptor = idToEncryptor.get(id);
        if (encryptor == null) {
            return fallback.decrypt(cipherText);
        }
        return encryptor.decrypt(extractCipherText(cipherText));
    }

    private String extractId(String cipherText) {
        if (cipherText == null) {
            return null;
        }

        int start = cipherText.indexOf(PREFIX);

        if (start != 0) {
            return null;
        }

        int end = cipherText.indexOf(SUFFIX, start);

        if (end < 0) {
            return null;
        }

        return cipherText.substring(start + PREFIX.length(), end);
    }

    private String extractCipherText(String cipherText) {
        int start = cipherText.indexOf(SUFFIX);

        return cipherText.substring(start + SUFFIX.length());
    }
}
