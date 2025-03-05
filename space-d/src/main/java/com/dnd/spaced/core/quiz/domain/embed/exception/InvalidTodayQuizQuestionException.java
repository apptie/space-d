package com.dnd.spaced.core.quiz.domain.embed.exception;

import com.dnd.spaced.global.exception.base.QuizServerException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class InvalidTodayQuizQuestionException extends QuizServerException {

    public InvalidTodayQuizQuestionException(String message) {
        super(QuizErrorCode.INVALID_TODAY_QUIZ_QUESTION_EXCEPTION, message);
    }
}
