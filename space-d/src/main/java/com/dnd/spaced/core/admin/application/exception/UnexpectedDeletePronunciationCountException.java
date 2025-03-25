package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class UnexpectedDeletePronunciationCountException extends WordClientException {

    public UnexpectedDeletePronunciationCountException(String message) {
        super(WordErrorCode.UNEXPECTED_DELETE_PRONUNCIATION_COUNT_EXCEPTION, message);
    }
}
