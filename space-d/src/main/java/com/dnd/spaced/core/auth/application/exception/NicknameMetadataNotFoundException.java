package com.dnd.spaced.core.auth.application.exception;

import com.dnd.spaced.global.exception.base.AuthServerException;
import com.dnd.spaced.global.exception.code.AuthErrorCode;

public class NicknameMetadataNotFoundException extends AuthServerException {

    public NicknameMetadataNotFoundException(String message) {
        super(AuthErrorCode.NICKNAME_METADATA_NOT_FOUND_EXCEPTION, message);
    }
}
