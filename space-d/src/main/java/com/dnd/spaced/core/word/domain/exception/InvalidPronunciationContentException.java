package com.dnd.spaced.core.word.domain.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class InvalidPronunciationContentException extends AccountClientException {

    public InvalidPronunciationContentException(String message) {
        super(WordErrorCode.INVALID_PRONUNCIATION_CONTENT, message);
    }
}
