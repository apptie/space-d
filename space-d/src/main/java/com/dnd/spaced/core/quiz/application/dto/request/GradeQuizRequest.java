package com.dnd.spaced.core.quiz.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record GradeQuizRequest(@Size(min = 5, max = 5) SubmitAnswerRequest[] submitAnswers) {

    public record SubmitAnswerRequest(@Positive Long wordId, @NotEmpty String content) {
    }
}
