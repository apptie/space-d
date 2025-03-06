package com.dnd.spaced.core.quiz.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateQuizRequest(@NotBlank String quizCategoryName) {
}
