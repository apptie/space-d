package com.dnd.spaced.core.quiz.domain.exception;

import com.dnd.spaced.global.exception.base.QuizServerException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class InvalidQuizOptionContentException extends QuizServerException {

    public InvalidQuizOptionContentException(String message) {
        super(QuizErrorCode.INVALID_QUIZ_OPTION_CONTENT_EXCEPTION, message);
    }
}
