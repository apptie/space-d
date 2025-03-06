package com.dnd.spaced.core.quiz.application.dto.request;

import jakarta.validation.constraints.Size;

public record GradeQuizRequest(@Size(min = 5, max = 5) int[] answers) {
}
