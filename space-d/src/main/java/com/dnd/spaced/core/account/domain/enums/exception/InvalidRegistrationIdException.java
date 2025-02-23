package com.dnd.spaced.core.account.domain.enums.exception;

import com.dnd.spaced.global.exception.base.AccountServerException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidRegistrationIdException extends AccountServerException {

    public InvalidRegistrationIdException(String message) {
        super(AccountErrorCode.INVALID_REGISTRATION_ID, message);
    }
}
