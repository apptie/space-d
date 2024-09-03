package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseServerException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseServerException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseServerException(ErrorCode errorCode, String message, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;
    }
}
