package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse.TodayQuizOptionResponse;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TodayQuizApplicationMapper {

    public static TodayQuizGradedAnswerCollectionResponse toDto(List<TodayQuizGradedAnswer> todayQuizGradedAnswers) {
        List<TodayQuizGradedAnswerResponse> responses = todayQuizGradedAnswers.stream()
                                                                              .map(TodayQuizApplicationMapper::toDto)
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
                quizQuestion.getTodayQuizAnswerOption().getContent(),
                todayQuizGradedAnswer.isCorrect()
        );
    }

    public static TodayQuizResponse toDto(TodayQuiz todayQuiz) {
        TodayQuizQuestion quizQuestion = todayQuiz.getTodayQuizQuestion();

        return new TodayQuizResponse(todayQuiz.getId(), toQuizDto(quizQuestion));
    }

    private static TodayQuizQuestionResponse toGradedAnswerDto(TodayQuizQuestion quizQuestion) {
        return new TodayQuizQuestionResponse(
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestion(),
                quizQuestion.getQuestionContent()
        );
    }

    private static TodayQuizResponse.TodayQuizQuestionResponse toQuizDto(TodayQuizQuestion quizQuestion) {
        // TODO : TodayQuizOption 목록 조회 필요
        List<TodayQuizOptionResponse> quizOptionDtos = Collections.emptyList();

        return new TodayQuizResponse.TodayQuizQuestionResponse(
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestion(),
                quizQuestion.getQuestionContent(),
                quizOptionDtos,
                quizQuestion.getTodayQuizAnswerOption().getWordId()
        );
    }

    private static TodayQuizOptionResponse toTodayQuizQuestionDto(
            TodayQuizOption quizOption
    ) {
        return new TodayQuizOptionResponse(quizOption.getId(), quizOption.getContent());
    }
}
