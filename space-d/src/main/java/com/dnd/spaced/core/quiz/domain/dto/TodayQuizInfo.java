package com.dnd.spaced.core.quiz.domain.dto;

import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import java.util.List;

public record TodayQuizInfo(
        Long id,
        QuizCategory quizCategory,
        String question,
        String questionContent,
        TodayQuizAnswerOption todayQuizAnswerOption,
        List<TodayQuizOptionInfo> todayQuizOptions
) {

    public record TodayQuizOptionInfo(Long id, Long wordId, String content, int optionOrder) {
    }
}

