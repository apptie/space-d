package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class AccountClientException extends BaseClientException {

    public AccountClientException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AccountClientException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
