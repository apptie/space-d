package com.dnd.spaced.core.quiz.domain.dto;

import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import java.time.LocalDateTime;
import java.util.List;

public record QuizInfo(
        Long id,
        Long accountId,
        boolean solved,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<QuizQuestionInfo> quizQuestions
) {

    public record QuizQuestionInfo(
            Long id,
            QuizCategory quizCategory,
            QuizAnswerOption quizAnswerOption,
            String questionContent,
            String questionExample,
            List<QuizOptionInfo> quizOptions
    ) {

        public record QuizOptionInfo(
                Long id,
                Long wordId,
                String content,
                int optionOrder
        ) {
        }
    }
}
