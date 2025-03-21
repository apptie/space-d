package com.dnd.spaced.core.quiz.application.dto.response;

import java.util.List;
import lombok.Getter;

public record TodayQuizResponse(Long id, TodayQuizQuestionResponse todayQuizQuestion, TodayQuizStatus todayQuizStatus) {

    public record TodayQuizQuestionResponse(
            String quizCategory,
            String question,
            String questionContent,
            List<TodayQuizOptionResponse> todayQuizOptions,
            Long answerWordId,
            String answerContent
    ) {

        public record TodayQuizOptionResponse(Long id, Long wordId, String content, int optionOrder) {
        }
    }

    @Getter
    public enum TodayQuizStatus {
        NOT_LOGGED_IN("인증 전"),
        NOT_SOLVED("풀이 전"),
        SOLVED("풀이 후");

        private final String name;

        TodayQuizStatus(String name) {
            this.name = name;
        }
    }
}
