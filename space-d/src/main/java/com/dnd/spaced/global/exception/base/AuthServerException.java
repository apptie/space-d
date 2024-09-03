package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class AuthServerException extends BaseClientException {

    public AuthServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthServerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
