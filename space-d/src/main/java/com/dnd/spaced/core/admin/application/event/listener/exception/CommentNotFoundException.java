package com.dnd.spaced.core.admin.application.event.listener.exception;

import com.dnd.spaced.global.exception.base.ReportServerException;
import com.dnd.spaced.global.exception.code.CommentErrorCode;

public class CommentNotFoundException extends ReportServerException {

    public CommentNotFoundException(String message) {
        super(CommentErrorCode.COMMENT_NOT_FOUND, message);
    }
}
