package com.dnd.spaced.core.report.application.exception;

import com.dnd.spaced.global.exception.base.ReportClientException;
import com.dnd.spaced.global.exception.code.ReportErrorCode;

public class CannotReportOwnCommentException extends ReportClientException {

    public CannotReportOwnCommentException(String message) {
        super(ReportErrorCode.CANNOT_REPORT_OWN_COMMENT_EXCEPTION, message);
    }
}
