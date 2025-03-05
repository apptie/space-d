package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class QuizServerException extends BaseServerException {

    public QuizServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public QuizServerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
