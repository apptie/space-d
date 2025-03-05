package com.dnd.spaced.core.quiz.application.exception;

import com.dnd.spaced.global.exception.base.QuizServerException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class TodayQuizNotFoundException extends QuizServerException {

    public TodayQuizNotFoundException(String message) {
        super(QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION, message);
    }
}
