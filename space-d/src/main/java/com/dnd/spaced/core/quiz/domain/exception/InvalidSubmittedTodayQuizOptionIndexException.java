package com.dnd.spaced.core.quiz.domain.exception;

import com.dnd.spaced.global.exception.base.QuizClientException;
import com.dnd.spaced.global.exception.code.QuizErrorCode;

public class InvalidSubmittedTodayQuizOptionIndexException extends QuizClientException {

    public InvalidSubmittedTodayQuizOptionIndexException( String message) {
        super(QuizErrorCode.INVALID_SUBMITTED_QUIZ_OPTION_INDEX_EXCEPTION, message);
    }
}
