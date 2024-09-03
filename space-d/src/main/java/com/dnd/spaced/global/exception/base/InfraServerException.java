package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public abstract class InfraServerException extends BaseServerException {

    public InfraServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InfraServerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
