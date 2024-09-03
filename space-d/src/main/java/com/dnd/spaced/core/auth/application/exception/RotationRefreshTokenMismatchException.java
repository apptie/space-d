package com.dnd.spaced.core.auth.application.exception;

import com.dnd.spaced.global.exception.base.AuthClientException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class RotationRefreshTokenMismatchException extends AuthClientException {

    public RotationRefreshTokenMismatchException(String message) {
        super(AuthErrorCode.ROTATION_REFRESH_TOKEN_MISMATCH, message);
    }
}
