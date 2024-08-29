package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class AuthClientException extends BaseClientException {

    public AuthClientException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthClientException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
