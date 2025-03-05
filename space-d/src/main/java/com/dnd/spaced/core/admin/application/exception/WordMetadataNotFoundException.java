package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.QuizServerException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class WordMetadataNotFoundException extends QuizServerException {

    public WordMetadataNotFoundException(String message) {
        super(QuizErrorCode.WORD_METADATA_NOT_FOUND_EXCEPTION, message);
    }
}
