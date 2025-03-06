package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse.TodayQuizOptionResponse;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TodayQuizApplicationMapper {

    public static TodayQuizGradedAnswerResponse toDto(TodayQuizGradedAnswer todayQuizGradedAnswer) {
        TodayQuiz quiz = todayQuizGradedAnswer.getTodayQuiz();
        TodayQuizQuestion quizQuestion = quiz.getTodayQuizQuestion();
        List<TodayQuizOption> quizOptions = quizQuestion.getTodayQuizOptions();

        return new TodayQuizGradedAnswerResponse(
                todayQuizGradedAnswer.getId(),
                quiz.getId(),
                todayQuizGradedAnswer.getAccountId(),
                toGradedAnswerDto(quizQuestion),
                getSubmittedOptionContent(quizOptions, todayQuizGradedAnswer.getSelectedOptionIndex()),
                getAnswerOptionContent(quizQuestion, todayQuizGradedAnswer),
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

    private static String getAnswerOptionContent(TodayQuizQuestion question, TodayQuizGradedAnswer gradedAnswer) {
        List<TodayQuizOption> options = question.getTodayQuizOptions();
        int submittedOptionIndex = gradedAnswer.getSelectedOptionIndex();

        return options.get(submittedOptionIndex)
                          .getContent();
    }

    private static String getSubmittedOptionContent(List<TodayQuizOption> quizOptions, int submittedOptionIndex) {
        return quizOptions.get(submittedOptionIndex)
                          .getContent();
    }

    private static TodayQuizResponse.TodayQuizQuestionResponse toQuizDto(TodayQuizQuestion quizQuestion) {
        List<TodayQuizOptionResponse> quizOptionDtos = quizQuestion.getTodayQuizOptions()
                                                                   .stream()
                                                                   .map(TodayQuizApplicationMapper::toTodayQuizQuestionDto)
                                                                   .toList();

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
