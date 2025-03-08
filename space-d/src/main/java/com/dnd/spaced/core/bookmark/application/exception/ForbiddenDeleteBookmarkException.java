package com.dnd.spaced.core.bookmark.application.exception;

import com.dnd.spaced.global.exception.base.BookmarkClientException;
import com.dnd.spaced.global.exception.code.BookmarkErrorCode;

public class ForbiddenDeleteBookmarkException extends BookmarkClientException {

    public ForbiddenDeleteBookmarkException(String message) {
        super(BookmarkErrorCode.FORBIDDEN_DELETE_BOOKMARK_EXCEPTION, message);
    }
}
