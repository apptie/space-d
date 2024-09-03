package com.dnd.spaced.core.account.domain.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidExperienceException extends AccountClientException {

    public InvalidExperienceException(String message) {
        super(AccountErrorCode.INVALID_EXPERIENCE, message);
    }
}
