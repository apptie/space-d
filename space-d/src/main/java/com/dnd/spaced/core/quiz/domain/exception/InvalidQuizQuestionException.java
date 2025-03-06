package com.dnd.spaced.core.quiz.domain.exception;

import com.dnd.spaced.global.exception.base.QuizServerException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class InvalidQuizQuestionException extends QuizServerException {

    public InvalidQuizQuestionException(String message) {
        super(QuizErrorCode.INVALID_QUIZ_QUESTION_CONTENT_EXCEPTION, message);
    }
}
