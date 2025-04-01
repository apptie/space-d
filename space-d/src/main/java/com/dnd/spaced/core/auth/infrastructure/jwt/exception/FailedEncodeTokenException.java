package com.dnd.spaced.core.auth.infrastructure.jwt.exception;

import com.dnd.spaced.global.exception.base.AuthClientException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class FailedEncodeTokenException extends AuthClientException {

    public FailedEncodeTokenException(String message, Throwable e) {
        super(AuthErrorCode.FAILED_ENCODE_TOKEN_EXCEPTION, message, e);
    }
}
