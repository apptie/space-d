package com.dnd.spaced.core.word.domain.exception;

import com.dnd.spaced.global.exception.base.WordClientException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class InvalidCategoryNameException extends WordClientException {

    public InvalidCategoryNameException(String message) {
        super(WordErrorCode.INVALID_CATEGORY_NAME, message);
    }
}
