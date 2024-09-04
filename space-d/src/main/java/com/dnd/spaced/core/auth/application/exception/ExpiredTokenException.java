package com.dnd.spaced.core.auth.application.exception;

import com.dnd.spaced.global.exception.base.AuthClientException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class ExpiredTokenException extends AuthClientException {

    public ExpiredTokenException(String message) {
        super(AuthErrorCode.EXPIRED_TOKEN, message);
    }
}
