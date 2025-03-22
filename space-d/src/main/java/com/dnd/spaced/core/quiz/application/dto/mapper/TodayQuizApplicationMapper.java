package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse.TodayQuizOptionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizStatus;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizInfo;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
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
                quizQuestion.getTodayQuizAnswerOption().getAnswerContent(),
                todayQuizGradedAnswer.isCorrect()
        );
    }

    public static TodayQuizResponse toDto(TodayQuiz todayQuiz, Long accountId, boolean solved) {
        TodayQuizResponse.TodayQuizQuestionResponse todayQuizQuestion = toTodayQuizQuestionDto(
                todayQuiz.getTodayQuizQuestion()
        );

        if (accountId == -1L) {
            return new TodayQuizResponse(todayQuiz.getId(), todayQuizQuestion, TodayQuizStatus.NOT_LOGGED_IN);
        }
        if (solved) {
            return new TodayQuizResponse(todayQuiz.getId(), todayQuizQuestion, TodayQuizStatus.SOLVED);
        }
        return new TodayQuizResponse(todayQuiz.getId(), todayQuizQuestion, TodayQuizStatus.NOT_SOLVED);
    }

    public static SimpleTodayQuizResponse toDto(SimpleTodayQuizInfo simpleTodayQuizInfo) {
        SimpleTodayQuizResponse.TodayQuizQuestionResponse todayQuizQuestionResponse = new SimpleTodayQuizResponse.TodayQuizQuestionResponse(
                simpleTodayQuizInfo.quizCategory().getName(),
                simpleTodayQuizInfo.question(),
                simpleTodayQuizInfo.questionContent()
        );

        return new SimpleTodayQuizResponse(
                simpleTodayQuizInfo.id(),
                todayQuizQuestionResponse,
                simpleTodayQuizInfo.createdAt()
        );
    }

    private static TodayQuizQuestionResponse toGradedAnswerDto(TodayQuizQuestion quizQuestion) {
        return new TodayQuizQuestionResponse(
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestion(),
                quizQuestion.getPassage()
        );
    }

    private static TodayQuizResponse.TodayQuizQuestionResponse toTodayQuizQuestionDto(
            TodayQuizQuestion todayQuizQuestion
    ) {
        List<TodayQuizOptionResponse> todayQuizOptionResponses = toTodayQuizOptionDto(todayQuizQuestion);

        return new TodayQuizResponse.TodayQuizQuestionResponse(
                todayQuizQuestion.getQuizCategory().getName(),
                todayQuizQuestion.getQuestion(),
                todayQuizQuestion.getPassage(),
                todayQuizOptionResponses,
                todayQuizQuestion.getTodayQuizAnswerOption().getAnswerWordId(),
                todayQuizQuestion.getTodayQuizAnswerOption().getAnswerContent()
        );
    }

    private static List<TodayQuizOptionResponse> toTodayQuizOptionDto(TodayQuizQuestion todayQuizQuestion) {
        return todayQuizQuestion.getTodayQuizOptions()
                                .stream()
                                .map(todayQuizOption ->
                                        new TodayQuizOptionResponse(
                                                todayQuizOption.getId(),
                                                todayQuizOption.getWordId(),
                                                todayQuizOption.getContent(),
                                                todayQuizOption.getOptionOrder()
                                        )
                                )
                                .toList();
    }
}
