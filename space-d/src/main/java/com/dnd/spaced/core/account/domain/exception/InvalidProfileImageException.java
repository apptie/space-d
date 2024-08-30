package com.dnd.spaced.core.account.domain.exception;

import com.dnd.spaced.global.exception.base.AccountServerException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidProfileImageException extends AccountServerException {

    public InvalidProfileImageException(String message) {
        super(AccountErrorCode.INVALID_PROFILE_IMAGE, message);
    }
}
