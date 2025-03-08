package com.dnd.spaced.core.report.application.exception;

import com.dnd.spaced.global.exception.base.ReportClientException;
import com.dnd.spaced.global.exception.code.ReportErrorCode;

public class ReportReasonNotFoundException extends ReportClientException {

    public ReportReasonNotFoundException(String message) {
        super(ReportErrorCode.REPORT_REASON_NOT_FOUND_EXCEPTION, message);
    }
}
