package com.dnd.spaced.core.account.application.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class ForbiddenAccountException extends AccountClientException {

    public ForbiddenAccountException(String message) {
        super(AccountErrorCode.FORBIDDEN_ACCOUNT, message);
    }
}
