package com.dnd.spaced.core.quiz.application.exception;

import com.dnd.spaced.global.exception.base.QuizClientException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class QuizNotFoundException extends QuizClientException {

    public QuizNotFoundException(String message) {
        super(QuizErrorCode.QUIZ_NOT_FOUND_EXCEPTION, message);
    }
}
