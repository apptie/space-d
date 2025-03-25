package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class WordServerException extends BaseClientException {

    public WordServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public WordServerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
