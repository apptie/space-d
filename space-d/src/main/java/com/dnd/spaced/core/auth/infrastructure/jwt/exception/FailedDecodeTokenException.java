package com.dnd.spaced.core.auth.infrastructure.jwt.exception;

import com.dnd.spaced.global.exception.base.AuthClientException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class FailedDecodeTokenException extends AuthClientException {

    public FailedDecodeTokenException(String message, Throwable e) {
        super(AuthErrorCode.FAILED_DECODE_TOKEN_EXCEPTION, message, e);
    }
}
