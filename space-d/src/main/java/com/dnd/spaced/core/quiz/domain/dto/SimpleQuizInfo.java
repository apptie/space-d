package com.dnd.spaced.core.quiz.domain.dto;

import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import java.time.LocalDateTime;
import java.util.List;

public record SimpleQuizInfo(
        Long id,
        Long accountId,
        boolean solved,
        LocalDateTime createdAt,
        List<QuizQuestionInfo> quizQuestions
) {

    public record QuizQuestionInfo(QuizCategory quizCategory, String questionExample) {
    }
}
