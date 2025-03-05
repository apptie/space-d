package com.dnd.spaced.core.quiz.application.exception;

import com.dnd.spaced.global.exception.base.QuizClientException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class InvalidQuizWordCountException extends QuizClientException {

    public InvalidQuizWordCountException(String message) {
        super(QuizErrorCode.INVALID_QUIZ_WORD_COUNT_EXCEPTION, message);
    }
}
