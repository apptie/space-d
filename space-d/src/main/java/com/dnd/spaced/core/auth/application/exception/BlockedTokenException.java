package com.dnd.spaced.core.auth.application.exception;

import com.dnd.spaced.global.exception.base.AuthClientException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class BlockedTokenException extends AuthClientException {

    public BlockedTokenException(String message) {
        super(AuthErrorCode.BLOCKED_TOKEN, message);
    }
}
