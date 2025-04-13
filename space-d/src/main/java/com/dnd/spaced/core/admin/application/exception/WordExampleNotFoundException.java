package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class WordExampleNotFoundException extends WordClientException {

    public WordExampleNotFoundException(String message) {
        super(WordErrorCode.WORD_EXAMPLE_NOT_FOUND_EXCEPTION, message);
    }
}
