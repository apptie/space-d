package com.dnd.spaced.core.quiz.application.dto.response;

public record TodayQuizGradedAnswerResponse(
        Long id,
        Long todayQuizId,
        Long accountId,
        TodayQuizQuestionResponse todayQuizQuestion,
        String selectedQuizOptionContent,
        String answerQuizOptionContent,
        boolean corrected
) {

    public record TodayQuizQuestionResponse(String quizCategory, String question, String passage) {
    }
}
