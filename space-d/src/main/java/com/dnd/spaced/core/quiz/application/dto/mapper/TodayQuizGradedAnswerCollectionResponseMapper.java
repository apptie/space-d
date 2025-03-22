package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse.TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TodayQuizGradedAnswerCollectionResponseMapper {

    public static TodayQuizGradedAnswerCollectionResponse toDto(List<TodayQuizGradedAnswer> todayQuizGradedAnswers) {
        List<TodayQuizGradedAnswerResponse> responses = todayQuizGradedAnswers.stream()
                                                                              .map(TodayQuizGradedAnswerCollectionResponseMapper::toDto)
                                                                              .toList();

        return new TodayQuizGradedAnswerCollectionResponse(responses);
    }

    public static TodayQuizGradedAnswerResponse toDto(TodayQuizGradedAnswer todayQuizGradedAnswer) {
        TodayQuiz quiz = todayQuizGradedAnswer.getTodayQuiz();
        TodayQuizQuestion quizQuestion = quiz.getTodayQuizQuestion();

        return new TodayQuizGradedAnswerResponse(
                todayQuizGradedAnswer.getId(),
                quiz.getId(),
                todayQuizGradedAnswer.getAccountId(),
                toGradedAnswerDto(quizQuestion),
                todayQuizGradedAnswer.getSelectedContent(),
                quizQuestion.getTodayQuizAnswerOption().getAnswerContent(),
                todayQuizGradedAnswer.isCorrect()
        );
    }

    private static TodayQuizQuestionResponse toGradedAnswerDto(TodayQuizQuestion quizQuestion) {
        return new TodayQuizQuestionResponse(
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestion(),
                quizQuestion.getPassage()
        );
    }
}
