package com.dnd.spaced.core.skill.application.event.dto;

import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import java.util.List;

public record GradedQuizEvent(Long accountId, Long correctCount) {

    public static GradedQuizEvent of(Long accountId, List<QuizGradedAnswer> quizGradedAnswers) {
        long correctCount = quizGradedAnswers.stream()
                                             .filter(QuizGradedAnswer::isCorrect)
                                             .count();

        return new GradedQuizEvent(accountId, correctCount);
    }
}
