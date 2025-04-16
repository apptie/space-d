package com.dnd.spaced.core.bookmark.application.exception;

import com.dnd.spaced.global.exception.base.BookmarkServerException;
import com.dnd.spaced.global.exception.code.BookmarkErrorCode;

public class BookmarkLockException extends BookmarkServerException {

    public BookmarkLockException(String message, Throwable e) {
        super(BookmarkErrorCode.BOOKMARK_LOCK_EXCEPTION, message, e);
    }
}
