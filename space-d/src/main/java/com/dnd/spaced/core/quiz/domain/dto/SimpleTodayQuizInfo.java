package com.dnd.spaced.core.quiz.domain.dto;

import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import java.time.LocalDateTime;

public record SimpleTodayQuizInfo(
        Long id,
        QuizCategory quizCategory,
        String question,
        String questionContent,
        TodayQuizAnswerOption todayQuizAnswerOption,
        LocalDateTime createdAt
) {
}

