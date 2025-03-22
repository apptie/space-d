package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TodayQuizGradedAnswerResponseMapper {

    public static TodayQuizGradedAnswerResponse toDto(TodayQuizGradedAnswer todayQuizGradedAnswer) {
        TodayQuiz quiz = todayQuizGradedAnswer.getTodayQuiz();
        TodayQuizQuestion quizQuestion = quiz.getTodayQuizQuestion();

        return new TodayQuizGradedAnswerResponse(
                todayQuizGradedAnswer.getId(),
                quiz.getId(),
                todayQuizGradedAnswer.getAccountId(),
                toDto(quizQuestion),
                todayQuizGradedAnswer.getSelectedContent(),
                quizQuestion.getTodayQuizAnswerOption().getAnswerContent(),
                todayQuizGradedAnswer.isCorrect()
        );
    }

    private static TodayQuizQuestionResponse toDto(TodayQuizQuestion quizQuestion) {
        return new TodayQuizQuestionResponse(
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestion(),
                quizQuestion.getPassage()
        );
    }
}
