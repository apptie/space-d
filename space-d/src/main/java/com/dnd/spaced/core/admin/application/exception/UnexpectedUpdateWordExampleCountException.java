package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class UnexpectedUpdateWordExampleCountException extends WordClientException {

    public UnexpectedUpdateWordExampleCountException(String message) {
        super(WordErrorCode.UNEXPECTED_UPDATE_WORD_EXAMPLE_COUNT_EXCEPTION, message);
    }
}
