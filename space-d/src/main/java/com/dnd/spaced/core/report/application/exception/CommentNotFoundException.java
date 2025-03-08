package com.dnd.spaced.core.report.application.exception;

import com.dnd.spaced.global.exception.base.ReportClientException;
import com.dnd.spaced.global.exception.code.ReportErrorCode;

public class CommentNotFoundException extends ReportClientException {

    public CommentNotFoundException(String message) {
        super(ReportErrorCode.COMMENT_NOT_FOUND_EXCEPTION, message);
    }
}
