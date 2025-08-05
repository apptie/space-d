package com.dnd.spaced.core.quiz.domain.dto;

import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import java.util.List;

public record TodayQuizDto(
        Long id,
        QuizCategory quizCategory,
        String question,
        String questionContent,
        TodayQuizAnswerOption todayQuizAnswerOption,
        List<TodayQuizOptionDto> todayQuizOptions
) {

    public record TodayQuizOptionDto(Long id, Long wordId, String content, int optionOrder) {
    }
}

