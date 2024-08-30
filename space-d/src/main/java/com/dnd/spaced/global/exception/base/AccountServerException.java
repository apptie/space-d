package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class AccountServerException extends BaseServerException {

    public AccountServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AccountServerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
