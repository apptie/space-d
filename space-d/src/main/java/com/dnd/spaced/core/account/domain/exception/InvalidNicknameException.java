package com.dnd.spaced.core.account.domain.exception;

import com.dnd.spaced.global.exception.base.AccountServerException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidNicknameException extends AccountServerException {

    public InvalidNicknameException(String message) {
        super(AccountErrorCode.INVALID_NICKNAME, message);
    }
}
