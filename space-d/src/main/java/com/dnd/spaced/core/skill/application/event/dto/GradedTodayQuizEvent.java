package com.dnd.spaced.core.skill.application.event.dto;

public record GradedTodayQuizEvent(Long accountId, long correctCount) {

    private static final long TODAY_QUIZ_CORRECT_COUNT = 1L;
    private static final long TODAY_QUIZ_WRONG_COUNT = 0L;

    public static GradedTodayQuizEvent of(Long accountId, boolean corrected) {
        if (corrected) {
            return new GradedTodayQuizEvent(accountId, TODAY_QUIZ_CORRECT_COUNT);
        }

        return new GradedTodayQuizEvent(accountId, TODAY_QUIZ_WRONG_COUNT);
    }
}
