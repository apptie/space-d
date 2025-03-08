package com.dnd.spaced.core.bookmark.application.exception;

import com.dnd.spaced.global.exception.base.BookmarkClientException;
import com.dnd.spaced.global.exception.code.BookmarkErrorCode;

public class BookmarkNotFoundException extends BookmarkClientException {

    public BookmarkNotFoundException(String message) {
        super(BookmarkErrorCode.BOOKMARK_NOT_FOUND_EXCEPTION, message);
    }
}
