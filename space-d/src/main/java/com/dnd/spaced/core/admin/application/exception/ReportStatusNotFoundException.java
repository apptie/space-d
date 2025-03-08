package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.ReportClientException;
import com.dnd.spaced.global.exception.code.ReportErrorCode;

public class ReportStatusNotFoundException extends ReportClientException {

    public ReportStatusNotFoundException(String message) {
        super(ReportErrorCode.REPORT_STATUS_NOT_FOUND_EXCEPTION, message);
    }
}
