package com.dnd.spaced.core.comment.application.exception;

import com.dnd.spaced.global.exception.base.CommentClientException;
import com.dnd.spaced.global.exception.code.CommentErrorCode;

public class ForbiddenCommentException extends CommentClientException {

    public ForbiddenCommentException(String message) {
        super(CommentErrorCode.FORBIDDEN_COMMENT, message);
    }
}
