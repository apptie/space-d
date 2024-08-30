package com.dnd.spaced.core.account.domain.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidEmailException extends AccountClientException {

    public InvalidEmailException(String message) {
        super(AccountErrorCode.INVALID_EMAIL, message);
    }
}
