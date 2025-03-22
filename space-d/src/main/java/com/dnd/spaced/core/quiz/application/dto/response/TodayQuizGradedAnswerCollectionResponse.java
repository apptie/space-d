package com.dnd.spaced.core.quiz.application.dto.response;

import java.util.List;

public record TodayQuizGradedAnswerCollectionResponse(List<TodayQuizGradedAnswerResponse> answers) {

    public record TodayQuizGradedAnswerResponse(
            Long id,
            Long todayQuizId,
            Long accountId,
            TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse todayQuizQuestion,
            String selectedQuizOptionContent,
            String answerQuizOptionContent,
            boolean corrected
    ) {

        public record TodayQuizQuestionResponse(String quizCategory, String question, String passage) {
        }
    }
}
