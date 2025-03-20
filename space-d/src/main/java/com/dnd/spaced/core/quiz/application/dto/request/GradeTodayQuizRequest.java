package com.dnd.spaced.core.quiz.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record GradeTodayQuizRequest(@Positive Long selectedWordId, @NotBlank String selectedContent) {
}
