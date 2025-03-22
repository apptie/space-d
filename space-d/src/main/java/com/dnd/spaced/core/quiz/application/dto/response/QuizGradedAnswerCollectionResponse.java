package com.dnd.spaced.core.quiz.application.dto.response;

import java.util.List;

public record QuizGradedAnswerCollectionResponse(List<QuizGradedAnswerResponse> answers, Long lastGradedAnswerId) {

    public record QuizGradedAnswerResponse(
            Long id,
            Long accountId,
            Long quizId,
            QuizQuestionResponse quizQuestion,
            String selectedQuizOptionContent,
            String answerQuizOptionContent,
            boolean corrected
    ) {

        public record QuizQuestionResponse(Long id, String quizCategory, String question, String passage) {
        }
    }
}
