package com.dnd.spaced.core.account.domain.exception;

import com.dnd.spaced.global.exception.base.AccountServerException;
import com.dnd.spaced.global.exception.code.AccountErrorCode;

public class InvalidNicknameMetadataException extends AccountServerException {

    public InvalidNicknameMetadataException(String message) {
        super(AccountErrorCode.INVALID_NICKNAME_METADATA, message);
    }
}
