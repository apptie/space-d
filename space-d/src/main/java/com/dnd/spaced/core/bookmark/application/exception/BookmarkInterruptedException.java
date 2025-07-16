package com.dnd.spaced.core.bookmark.application.exception;

import com.dnd.spaced.global.exception.base.BookmarkServerException;
import com.dnd.spaced.global.exception.code.BookmarkErrorCode;

public class BookmarkInterruptedException extends BookmarkServerException {

    public BookmarkInterruptedException(String message, Throwable e) {
        super(BookmarkErrorCode.BOOKMARK_INTERRUPTED_EXCEPTION, message, e);
    }
}
