package com.dnd.spaced.core.quiz.application.exception;

import com.dnd.spaced.global.exception.base.QuizClientException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class AlreadyGradeQuizException extends QuizClientException {

    public AlreadyGradeQuizException(String message) {
        super(QuizErrorCode.ALREADY_GRADE_QUIZ_EXCEPTION, message);
    }
}
