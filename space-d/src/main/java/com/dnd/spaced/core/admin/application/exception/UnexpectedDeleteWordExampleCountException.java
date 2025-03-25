package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class UnexpectedDeleteWordExampleCountException extends WordClientException {

    public UnexpectedDeleteWordExampleCountException(String message) {
        super(WordErrorCode.UNEXPECTED_DELETE_WORD_EXAMPLE_COUNT_EXCEPTION, message);
    }
}
