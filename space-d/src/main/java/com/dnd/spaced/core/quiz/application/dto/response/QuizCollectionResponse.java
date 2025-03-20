package com.dnd.spaced.core.quiz.application.dto.response;

import java.util.List;

public record QuizCollectionResponse(List<QuizResponse> quizzes, Long lastQuizId) {

    public record QuizResponse(Long id, Long accountId, boolean solved) {
    }
}
