package com.dnd.spaced.core.account.domain.enums.exception;

import com.dnd.spaced.global.exception.base.AccountServerException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidRoleNameException extends AccountServerException {

    public InvalidRoleNameException(String message) {
        super(AccountErrorCode.INVALID_ROLE, message);
    }
}
