package com.dnd.spaced.core.quiz.application.dto.request;

import jakarta.annotation.Nullable;

public record ReadQuizGradedAnswerSearchRequest(@Nullable Long lastQuizGradedAnswerId) {
}
