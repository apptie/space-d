package com.dnd.spaced.core.word.application.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class WordNotFoundException extends WordClientException {

    public WordNotFoundException(String message) {
        super(WordErrorCode.WORD_NOT_FOUND, message);
    }
}
