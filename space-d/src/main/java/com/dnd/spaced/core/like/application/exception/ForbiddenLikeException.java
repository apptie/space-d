package com.dnd.spaced.core.like.application.exception;

import com.dnd.spaced.global.exception.base.LikeClientException;
import com.dnd.spaced.global.exception.code.LikeErrorCode;

public class ForbiddenLikeException extends LikeClientException {

    public ForbiddenLikeException(String message) {
        super(LikeErrorCode.FORBIDDEN_LIKE, message);
    }
}
