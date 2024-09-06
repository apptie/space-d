package com.dnd.spaced.core.word.domain.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class InvalidPronunciationTypeNameException extends WordClientException {

    public InvalidPronunciationTypeNameException(String message) {
        super(WordErrorCode.INVALID_PRONUNCIATION_TYPE_NAME, message);
    }
}
