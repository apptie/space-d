package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class PronunciationDeletionNotAllowedException extends WordClientException {

    public PronunciationDeletionNotAllowedException(String message) {
        super(WordErrorCode.PRONUNCIATION_DELETION_NOT_ALLOWED, message);
    }
}
