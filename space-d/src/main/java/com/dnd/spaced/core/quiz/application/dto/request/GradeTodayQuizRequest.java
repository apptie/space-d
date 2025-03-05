package com.dnd.spaced.core.quiz.application.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record GradeTodayQuizRequest(@PositiveOrZero int answer) {
}
