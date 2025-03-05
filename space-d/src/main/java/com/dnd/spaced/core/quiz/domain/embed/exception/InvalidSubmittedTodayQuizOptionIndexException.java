package com.dnd.spaced.core.quiz.domain.embed.exception;

import com.dnd.spaced.global.exception.base.QuizServerException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class InvalidSubmittedTodayQuizOptionIndexException extends QuizServerException {

    public InvalidSubmittedTodayQuizOptionIndexException(String message) {
        super(QuizErrorCode.INVALID_SUBMITTED_TODAY_QUIZ_OPTION_INDEX_EXCEPTION, message);
    }
}
