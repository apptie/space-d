package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class WordExampleDeletionNotAllowedException extends WordClientException {

    public WordExampleDeletionNotAllowedException(String message) {
        super(WordErrorCode.WORD_EXAMPLE_DELETION_NOT_ALLOWED, message);
    }
}
