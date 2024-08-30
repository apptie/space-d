package com.dnd.spaced.core.auth.domain.exception;

import com.dnd.spaced.global.exception.base.AuthServerException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class InvalidBlacklistTokenContentException extends AuthServerException {

    public InvalidBlacklistTokenContentException(String message) {
        super(AuthErrorCode.INVALID_BLACKLIST_TOKEN_CONTENT, message);
    }
}
