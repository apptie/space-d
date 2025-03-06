package com.dnd.spaced.core.quiz.application.dto.response;

import java.util.List;

public record QuizResponse(Long id, Long accountId, List<QuizQuestionResponse> quizQuestions) {

    public record QuizQuestionResponse(
            Long id,
            String quizCategory,
            String question,
            String questionContent,
            List<QuizOptionResponse> quizOptions,
            Long answerOptionWordId
    ) {

        public record QuizOptionResponse(Long id, String content) {
        }
    }
}
