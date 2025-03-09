package com.dnd.spaced.global.exception.base;

import com.dnd.spaced.global.exception.code.ErrorCode;

public class SkillServerException extends BaseServerException {

    public SkillServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public SkillServerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }
}
