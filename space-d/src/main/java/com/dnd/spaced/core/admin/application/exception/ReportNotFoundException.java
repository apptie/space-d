package com.dnd.spaced.core.admin.application.exception;

import com.dnd.spaced.global.exception.base.ReportClientException;
import com.dnd.spaced.global.exception.code.ReportErrorCode;

public class ReportNotFoundException extends ReportClientException {

    public ReportNotFoundException(String message) {
        super(ReportErrorCode.REPORT_NOT_FOUND_EXCEPTION, message);
    }
}
