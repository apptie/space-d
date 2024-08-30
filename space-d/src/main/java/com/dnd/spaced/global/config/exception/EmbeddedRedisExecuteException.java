package com.dnd.spaced.global.config.exception;

import com.dnd.spaced.global.exception.base.InfraServerException;
import com.dnd.spaced.global.exception.code.InfraErrorCode;

public class EmbeddedRedisExecuteException extends InfraServerException {

    public EmbeddedRedisExecuteException(String message, Throwable e) {
        super(InfraErrorCode.CANNOT_EXECUTE_EMBEDDED_REDIS, message, e);
    }
}
