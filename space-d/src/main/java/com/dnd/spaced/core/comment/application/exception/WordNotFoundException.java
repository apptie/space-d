package com.dnd.spaced.core.comment.application.exception;

import com.dnd.spaced.global.exception.base.CommentClientException;
import com.dnd.spaced.global.exception.code.CommentErrorCode;

public class WordNotFoundException extends CommentClientException {

    public WordNotFoundException(String message) {
        super(CommentErrorCode.WORD_NOT_FOUND_EXCEPTION, message);
    }
}
