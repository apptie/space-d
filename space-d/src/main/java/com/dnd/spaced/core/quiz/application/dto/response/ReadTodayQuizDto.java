package com.dnd.spaced.core.quiz.application.dto.response;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;

public record ReadTodayQuizDto(TodayQuiz todayQuiz, boolean solved) {
}
