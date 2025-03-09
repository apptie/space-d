package com.dnd.spaced.core.skill.application.dto.response;

public record SkillResponse(
        Long accountId,
        long submitQuizQuestionCount,
        long quizQuestionCorrectCount,
        long submitTodayQuizQuestionCount,
        long todayQuizQuestionCorrectCount,
        double totalQuizQuestionCorrectPercent,
        double totalTodayQuizQuestionCorrectPercent
) {
}
