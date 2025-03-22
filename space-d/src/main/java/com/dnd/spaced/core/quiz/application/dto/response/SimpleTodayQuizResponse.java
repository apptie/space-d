package com.dnd.spaced.core.quiz.application.dto.response;

import java.time.LocalDateTime;

public record SimpleTodayQuizResponse(Long id, TodayQuizQuestionResponse todayQuizQuestion, LocalDateTime createdAt) {

    public record TodayQuizQuestionResponse(String quizCategory, String question, String passage) {
    }
}
