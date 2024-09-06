package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class WordClientException extends BaseClientException {

    public WordClientException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public WordClientException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
