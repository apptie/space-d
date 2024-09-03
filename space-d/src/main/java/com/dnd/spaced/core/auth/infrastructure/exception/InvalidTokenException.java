package com.dnd.spaced.core.auth.infrastructure.exception;

import com.dnd.spaced.global.exception.base.AuthClientException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class InvalidTokenException extends AuthClientException {

    public InvalidTokenException(String message) {
        super(AuthErrorCode.INVALID_TOKEN, message);
    }

    public InvalidTokenException(String message, Throwable e) {
        super(AuthErrorCode.INVALID_TOKEN, message, e);
    }
}
