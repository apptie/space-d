package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse.QuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse.QuizQuestionResponse.QuizOptionResponse;
import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo.QuizQuestionInfo;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo.QuizQuestionInfo.QuizOptionInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizApplicationMapper {

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

    public static QuizGradedAnswerCollectionResponse toDto(List<QuizGradedAnswer> quizGradedAnswers) {
        if (quizGradedAnswers.isEmpty()) {
            return new QuizGradedAnswerCollectionResponse(List.of(), null);
        }

        List<QuizGradedAnswerResponse> responses = quizGradedAnswers.stream()
                                                                    .map(QuizApplicationMapper::toDto)
                                                                    .toList();

        return new QuizGradedAnswerCollectionResponse(responses, responses.get(responses.size() - 1).id());
    }

    private static QuizGradedAnswerResponse toDto(QuizGradedAnswer quizGradedAnswer) {
        QuizQuestion question = quizGradedAnswer.getQuizQuestion();

        return new QuizGradedAnswerResponse(
                quizGradedAnswer.getId(),
                quizGradedAnswer.getAccountId(),
                quizGradedAnswer.getQuizId(),
                toGradedAnswerDto(question),
                question.getQuizAnswerOption().getAnswerContent(),
                quizGradedAnswer.getSelectedContent(),
                quizGradedAnswer.isCorrect()
        );
    }

    private static QuizGradedAnswerResponse.QuizQuestionResponse toGradedAnswerDto(QuizQuestion quizQuestion) {
        return new QuizGradedAnswerResponse.QuizQuestionResponse(
                quizQuestion.getId(),
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestion(),
                quizQuestion.getPassage()
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
                quizQuestion.quizAnswerOption().getAnswerWordId()
        );
    }

    private static QuizOptionResponse toQuizOptionDto(QuizOptionInfo quizOption) {
        return new QuizOptionResponse(quizOption.id(), quizOption.wordId(), quizOption.content());
    }
}
