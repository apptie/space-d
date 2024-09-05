package com.dnd.spaced.core.word.domain.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class InvalidWordMeaningException extends WordClientException {

    public InvalidWordMeaningException(String message) {
        super(WordErrorCode.INVALID_WORD_MEANING, message);
    }
}
