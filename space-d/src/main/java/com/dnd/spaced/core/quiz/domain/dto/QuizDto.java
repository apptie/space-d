package com.dnd.spaced.core.quiz.domain.dto;

import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import java.time.LocalDateTime;
import java.util.List;

public record QuizDto(
        Long id,
        Long accountId,
        boolean solved,
        LocalDateTime createdAt,
        List<QuizQuestionDto> quizQuestions
) {

    public record QuizQuestionDto(
            Long id,
            QuizCategory quizCategory,
            QuizAnswerOption quizAnswerOption,
            String questionContent,
            String questionExample,
            List<QuizOptionDto> quizOptions
    ) {

        public record QuizOptionDto(
                Long id,
                Long wordId,
                String content,
                int optionOrder
        ) {
        }
    }
}
