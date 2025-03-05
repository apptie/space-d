package com.dnd.spaced.core.quiz.application.enums.exception;

import com.dnd.spaced.global.exception.base.QuizClientException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class QuizCategoryNotFoundException extends QuizClientException {

    public QuizCategoryNotFoundException(String message) {
        super(QuizErrorCode.QUIZ_CATEGORY_NOT_FOUND_EXCEPTION, message);
    }
}
