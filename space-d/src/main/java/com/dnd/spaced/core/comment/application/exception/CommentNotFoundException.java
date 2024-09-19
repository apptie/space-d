package com.dnd.spaced.core.comment.application.exception;

import com.dnd.spaced.global.exception.base.CommentClientException;
import com.dnd.spaced.global.exception.code.CommentErrorCode;

public class CommentNotFoundException extends CommentClientException {

    public CommentNotFoundException(String message) {
        super(CommentErrorCode.COMMENT_NOT_FOUND, message);
    }
}
