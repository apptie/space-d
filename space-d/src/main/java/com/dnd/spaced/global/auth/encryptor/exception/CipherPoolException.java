package com.dnd.spaced.global.auth.encryptor.exception;

public class CipherPoolException extends RuntimeException {

    private static final String MESSAGE = "cipher pool에 문제가 발생했습니다.";

    public CipherPoolException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
