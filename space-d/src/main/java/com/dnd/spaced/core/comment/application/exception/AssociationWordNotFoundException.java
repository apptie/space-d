package com.dnd.spaced.core.comment.application.exception;

import com.dnd.spaced.global.exception.base.CommentClientException;
import com.dnd.spaced.global.exception.code.CommentErrorCode;

public class AssociationWordNotFoundException extends CommentClientException {

    public AssociationWordNotFoundException(String message) {
        super(CommentErrorCode.ASSOCIATION_WORD_NOT_FOUND, message);
    }
}
