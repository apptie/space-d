package com.dnd.spaced.core.quiz.application.dto.request;

import jakarta.annotation.Nullable;

public record ReadTodayQuizGradedAnswerSearchRequest(@Nullable Long lastTodayQuizGradedAnswerId) {
}
