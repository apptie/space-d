package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class ImageServerException extends BaseServerException {

    public ImageServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ImageServerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
