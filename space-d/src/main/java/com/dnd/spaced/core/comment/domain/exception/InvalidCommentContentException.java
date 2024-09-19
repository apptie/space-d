package com.dnd.spaced.core.comment.domain.exception;

import com.dnd.spaced.global.exception.base.CommentClientException;
import com.dnd.spaced.global.exception.code.CommentErrorCode;

public class InvalidCommentContentException extends CommentClientException {

    public InvalidCommentContentException(String message) {
        super(CommentErrorCode.INVALID_COMMENT_CONTENT, message);
    }
}
