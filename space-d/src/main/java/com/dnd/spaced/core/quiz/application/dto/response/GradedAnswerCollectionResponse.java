package com.dnd.spaced.core.quiz.application.dto.response;

import java.util.List;

public record GradedAnswerCollectionResponse(List<GradedAnswerResponse> answers, Long lastGradedAnswerId) {

    public record GradedAnswerResponse(
            Long id,
            Long accountId,
            Long quizId,
            QuizQuestionResponse quizQuestion,
            String selectedQuizOptionContent,
            String answerQuizOptionContent,
            boolean isCorrect
    ) {

        public record QuizQuestionResponse(Long id, String quizCategory, String question, String questionContent) {
        }
    }
}
