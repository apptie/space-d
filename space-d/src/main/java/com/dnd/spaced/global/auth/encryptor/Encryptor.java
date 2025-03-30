package com.dnd.spaced.global.auth.encryptor;

public interface Encryptor {

    String encrypt(String plainText);

    String decrypt(String cipherText);
}
