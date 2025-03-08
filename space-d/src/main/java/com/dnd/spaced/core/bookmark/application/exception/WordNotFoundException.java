package com.dnd.spaced.core.bookmark.application.exception;

import com.dnd.spaced.global.exception.base.BookmarkClientException;
import com.dnd.spaced.global.exception.code.BookmarkErrorCode;

public class WordNotFoundException extends BookmarkClientException {

    public WordNotFoundException(String message) {
        super(BookmarkErrorCode.WORD_NOT_FOUND_EXCEPTION, message);
    }
}
