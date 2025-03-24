package com.dnd.spaced.core.bookmark.application.exception;

import com.dnd.spaced.global.exception.base.BookmarkClientException;
import com.dnd.spaced.global.exception.code.BookmarkErrorCode;

public class AlreadyExistsBookmarkException extends BookmarkClientException {

    public AlreadyExistsBookmarkException(String message) {
        super(BookmarkErrorCode.ALREADY_EXISTS_BOOKMARK_EXCEPTION, message);
    }
}
