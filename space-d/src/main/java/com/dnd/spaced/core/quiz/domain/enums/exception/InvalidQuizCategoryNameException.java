package com.dnd.spaced.core.quiz.domain.enums.exception;

import com.dnd.spaced.global.exception.base.QuizServerException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class InvalidQuizCategoryNameException extends QuizServerException {

    public InvalidQuizCategoryNameException(String message) {
        super(QuizErrorCode.INVALID_QUIZ_CATEGORY_NAME_EXCEPTION, message);
    }
}
