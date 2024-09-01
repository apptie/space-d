package com.dnd.spaced.core.account.domain.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidIdException extends AccountClientException {

    public InvalidIdException(String message) {
        super(AccountErrorCode.INVALID_ID, message);
    }
}
