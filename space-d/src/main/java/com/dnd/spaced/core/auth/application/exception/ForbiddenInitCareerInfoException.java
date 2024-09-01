package com.dnd.spaced.core.auth.application.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class ForbiddenInitCareerInfoException extends AccountClientException {

    public ForbiddenInitCareerInfoException(String message) {
        super(AccountErrorCode.FORBIDDEN_INIT_CAREER_INFO, message);
    }
}
