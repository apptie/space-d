package com.dnd.spaced.core.admin.application.enums.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class WordMetadataCounterNotFoundException extends WordClientException {

    public WordMetadataCounterNotFoundException(String message) {
        super(WordErrorCode.WORD_METADATA_COUNTER_NOT_FOUND_EXCEPTION, message);
    }
}
