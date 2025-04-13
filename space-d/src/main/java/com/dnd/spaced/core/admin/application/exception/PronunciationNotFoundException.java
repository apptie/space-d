package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class PronunciationNotFoundException extends WordClientException {

    public PronunciationNotFoundException(String message) {
        super(WordErrorCode.PRONUNCIATION_NOT_FOUND_EXCEPTION, message);
    }
}
