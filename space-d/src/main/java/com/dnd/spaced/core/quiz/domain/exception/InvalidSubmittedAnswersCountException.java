package com.dnd.spaced.core.quiz.domain.exception;

import com.dnd.spaced.global.exception.base.QuizClientException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class InvalidSubmittedAnswersCountException extends QuizClientException {

    public InvalidSubmittedAnswersCountException(String message) {
        super(QuizErrorCode.INVALID_SUBMIT_ANSWERS_COUNT_EXCEPTION, message);
    }
}
