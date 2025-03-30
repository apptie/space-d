package com.dnd.spaced.global.auth.encryptor.exception;

public class DecryptException extends RuntimeException {

    private static final String MESSAGE = "토큰 복호화에 실패했습니다.";

    public DecryptException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
