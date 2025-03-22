package com.dnd.spaced.core.quiz.application.exception;

import com.dnd.spaced.global.exception.base.QuizClientException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class AlreadyGradeTodayQuizException extends QuizClientException {

    public AlreadyGradeTodayQuizException(String message) {
        super(QuizErrorCode.ALREADY_GRADE_QUIZ_EXCEPTION, message);
    }
}
