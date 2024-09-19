package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class LikeClientException extends BaseClientException {

    public LikeClientException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public LikeClientException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
