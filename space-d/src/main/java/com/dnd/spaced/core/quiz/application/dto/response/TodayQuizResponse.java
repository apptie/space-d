package com.dnd.spaced.core.quiz.application.dto.response;

import java.util.List;

public record TodayQuizResponse(Long id, TodayQuizQuestionResponse todayQuizQuestion) {

    public record TodayQuizQuestionResponse(
            String quizCategory,
            String question,
            String questionContent,
            List<TodayQuizOptionResponse> todayQuizOptions,
            Long answerWordId
    ) {

        public record TodayQuizOptionResponse(Long id, String content) {
        }
    }
}
