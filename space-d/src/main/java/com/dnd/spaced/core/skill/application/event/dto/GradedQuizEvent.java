package com.dnd.spaced.core.skill.application.event.dto;

import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import java.util.List;

public record GradedQuizEvent(Long accountId, Long correctCount) {

    public static GradedQuizEvent of(Long accountId, List<GradedAnswer> gradedAnswers) {
        long correctCount = gradedAnswers.stream()
                                         .filter(GradedAnswer::isCorrect)
                                         .count();

        return new GradedQuizEvent(accountId, correctCount);
    }
}
