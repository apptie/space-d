package com.dnd.spaced.core.word.domain.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class InvalidWordExampleContentException extends WordClientException {

    public InvalidWordExampleContentException(String message) {
        super(WordErrorCode.INVALID_WORD_EXAMPLE_CONTENT, message);
    }
}
