package com.dnd.spaced.core.quiz.domain.dto;

import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import java.time.LocalDateTime;
import java.util.List;

public record SimpleQuizDto(
        Long id,
        Long accountId,
        boolean solved,
        LocalDateTime createdAt,
        List<QuizQuestionDto> quizQuestions
) {

    public record QuizQuestionDto(QuizCategory quizCategory, String questionExample) {
    }
}
