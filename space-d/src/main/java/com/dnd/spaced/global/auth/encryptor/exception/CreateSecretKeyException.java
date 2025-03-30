package com.dnd.spaced.global.auth.encryptor.exception;

public class CreateSecretKeyException extends RuntimeException {

    private static final String MESSAGE = "Secret Key 생성에 실패했습니다.";

    public CreateSecretKeyException() {
        super(MESSAGE);
    }

    public CreateSecretKeyException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
