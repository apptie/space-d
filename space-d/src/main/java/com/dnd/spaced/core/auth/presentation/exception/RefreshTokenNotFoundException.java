package com.dnd.spaced.core.auth.presentation.exception;

import com.dnd.spaced.global.exception.base.AuthClientException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class RefreshTokenNotFoundException extends AuthClientException {

    public RefreshTokenNotFoundException(String message) {
        super(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, message);
    }
}
