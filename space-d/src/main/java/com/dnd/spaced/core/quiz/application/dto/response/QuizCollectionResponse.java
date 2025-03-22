package com.dnd.spaced.core.quiz.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record QuizCollectionResponse(List<QuizResponse> quizzes, Long lastQuizId) {

    public record QuizResponse(
            Long id,
            Long accountId,
            boolean solved,
            LocalDateTime createdAt,
            List<QuizQuestionResponse> quizQuestions
    ) {

        public record QuizQuestionResponse(String quizCategory, String passage) {
        }
    }
}
