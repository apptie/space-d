package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class BookmarkServerException extends BaseServerException {

    public BookmarkServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BookmarkServerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
