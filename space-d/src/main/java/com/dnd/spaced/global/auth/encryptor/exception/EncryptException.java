package com.dnd.spaced.global.auth.encryptor.exception;

public class EncryptException extends RuntimeException {

    private static final String MESSAGE = "토큰 암호화에 실패했습니다.";

    public EncryptException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
