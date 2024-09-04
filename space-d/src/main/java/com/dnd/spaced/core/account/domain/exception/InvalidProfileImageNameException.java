package com.dnd.spaced.core.account.domain.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidProfileImageNameException extends AccountClientException {

    public InvalidProfileImageNameException(String message) {
        super(AccountErrorCode.INVALID_PROFILE_NAME, message);
    }
}
