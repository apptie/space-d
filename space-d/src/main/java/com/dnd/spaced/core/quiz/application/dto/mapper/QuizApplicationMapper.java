package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse.GradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse.QuizQuestionResponse.QuizOptionResponse;
import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo.QuizQuestionInfo;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo.QuizQuestionInfo.QuizOptionInfo;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizApplicationMapper {

    public static QuizCollectionResponse toReadAllQuizDto(List<QuizInfo> quizzes) {
        if (quizzes.isEmpty()) {
            return new QuizCollectionResponse(Collections.emptyList(), null);
        }

        List<QuizCollectionResponse.QuizResponse> responses = quizzes.stream()
                                                                     .map(QuizApplicationMapper::toQuizResponse)
                                                                     .toList();
        return new QuizCollectionResponse(responses, responses.get(responses.size() - 1).id());
    }

    private static QuizCollectionResponse.QuizResponse toQuizResponse(QuizInfo quizInfo) {
        return new QuizCollectionResponse.QuizResponse(quizInfo.id(), quizInfo.accountId(), quizInfo.solved());
    }


    public static QuizResponse toDto(QuizInfo quiz) {
        List<QuizResponse.QuizQuestionResponse> quizQuestionResponses = quiz.quizQuestions()
                                                                            .stream()
                                                                            .map(QuizApplicationMapper::toQuizDto)
                                                                            .toList();

        return new QuizResponse(
                quiz.id(),
                quiz.accountId(),
                quizQuestionResponses
        );
    }

    public static GradedAnswerCollectionResponse toDto(List<QuizGradedAnswer> quizGradedAnswers) {
        if (quizGradedAnswers.isEmpty()) {
            return new GradedAnswerCollectionResponse(List.of(), null);
        }

        List<GradedAnswerResponse> responses = quizGradedAnswers.stream()
                                                                .map(QuizApplicationMapper::toDto)
                                                                .toList();

        return new GradedAnswerCollectionResponse(responses, responses.get(responses.size() - 1).id());
    }

    private static GradedAnswerResponse toDto(QuizGradedAnswer quizGradedAnswer) {
        QuizQuestion question = quizGradedAnswer.getQuizQuestion();

        return new GradedAnswerResponse(
                quizGradedAnswer.getId(),
                quizGradedAnswer.getAccountId(),
                quizGradedAnswer.getQuizId(),
                toGradedAnswerDto(question),
                question.getQuizAnswerOption().getContent(),
                quizGradedAnswer.getSelectedContent(),
                quizGradedAnswer.isCorrect()
        );
    }

    private static GradedAnswerResponse.QuizQuestionResponse toGradedAnswerDto(QuizQuestion quizQuestion) {
        return new GradedAnswerResponse.QuizQuestionResponse(
                quizQuestion.getId(),
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestionContent(),
                quizQuestion.getQuestionExample()
        );
    }

    private static QuizResponse.QuizQuestionResponse toQuizDto(QuizQuestionInfo quizQuestion) {
        List<QuizOptionResponse> quizOptionResponses = quizQuestion.quizOptions()
                                                                   .stream()
                                                                   .map(QuizApplicationMapper::toQuizOptionDto)
                                                                   .toList();

        return new QuizResponse.QuizQuestionResponse(
                quizQuestion.id(),
                quizQuestion.quizCategory().getName(),
                quizQuestion.questionContent(),
                quizQuestion.questionExample(),
                quizOptionResponses,
                quizQuestion.quizAnswerOption().getWordId()
        );
    }

    private static QuizOptionResponse toQuizOptionDto(QuizOptionInfo quizOption) {
        return new QuizOptionResponse(quizOption.id(), quizOption.content());
    }
}
