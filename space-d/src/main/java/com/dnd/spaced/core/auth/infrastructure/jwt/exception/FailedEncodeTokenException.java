package com.dnd.spaced.core.auth.infrastructure.jwt.exception;

import com.dnd.spaced.global.exception.base.AuthServerException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class FailedEncodeTokenException extends AuthServerException {

    public FailedEncodeTokenException(String message, Throwable e) {
        super(AuthErrorCode.FAILED_ENCODE_TOKEN_EXCEPTION, message, e);
    }
}
