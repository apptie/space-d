package com.dnd.spaced.global.config.exception;

import com.dnd.spaced.global.exception.base.InfraServerException;
import com.dnd.spaced.global.exception.code.InfraErrorCode;

public class EmbeddedRedisPortException extends InfraServerException {

    public EmbeddedRedisPortException(String message) {
        super(InfraErrorCode.NOT_FOUND_AVAILABLE_PORT, message);
    }
}
