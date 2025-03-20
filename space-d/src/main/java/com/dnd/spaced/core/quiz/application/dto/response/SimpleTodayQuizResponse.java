package com.dnd.spaced.core.quiz.application.dto.response;

public record SimpleTodayQuizResponse(Long id, TodayQuizQuestionResponse todayQuizQuestion) {

    public record TodayQuizQuestionResponse(String quizCategory, String question, String questionContent) {
    }
}
