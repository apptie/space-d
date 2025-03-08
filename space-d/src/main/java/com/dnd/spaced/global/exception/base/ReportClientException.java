package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class ReportClientException extends BaseClientException {

    public ReportClientException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }

    public ReportClientException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
