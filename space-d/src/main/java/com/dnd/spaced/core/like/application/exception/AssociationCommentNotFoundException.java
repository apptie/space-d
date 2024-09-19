package com.dnd.spaced.core.like.application.exception;

import com.dnd.spaced.global.exception.base.LikeClientException;
import com.dnd.spaced.global.exception.code.LikeErrorCode;

public class AssociationCommentNotFoundException extends LikeClientException {

    public AssociationCommentNotFoundException(String message) {
        super(LikeErrorCode.ASSOCIATION_COMMENT_NOT_FOUND, message);
    }
}
