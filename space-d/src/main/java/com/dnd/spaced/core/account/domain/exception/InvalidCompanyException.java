package com.dnd.spaced.core.account.domain.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidCompanyException extends AccountClientException {

    public InvalidCompanyException(String message) {
        super(AccountErrorCode.INVALID_COMPANY, message);
    }
}
