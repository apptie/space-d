package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse.GradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse.QuizQuestionResponse.QuizOptionResponse;
import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizApplicationMapper {

    public static QuizResponse toDto(Quiz quiz) {
        List<QuizResponse.QuizQuestionResponse> quizQuestionResponses = quiz.getQuizQuestions()
                                                                            .stream()
                                                                            .map(QuizApplicationMapper::toQuizDto)
                                                                            .toList();

        return new QuizResponse(
                quiz.getId(),
                quiz.getAccountId(),
                quizQuestionResponses
        );
    }

    public static GradedAnswerCollectionResponse toDto(List<GradedAnswer> gradedAnswers) {
        if (gradedAnswers.isEmpty()) {
            return new GradedAnswerCollectionResponse(List.of(), null);
        }

        List<GradedAnswerResponse> responses = gradedAnswers.stream()
                                                            .map(QuizApplicationMapper::toDto)
                                                            .toList();

        return new GradedAnswerCollectionResponse(responses, responses.get(responses.size() - 1).id());
    }

    private static GradedAnswerResponse toDto(GradedAnswer gradedAnswer) {
        QuizQuestion question = gradedAnswer.getQuizQuestion();

        return new GradedAnswerResponse(
                gradedAnswer.getId(),
                gradedAnswer.getAccountId(),
                gradedAnswer.getQuizId(),
                toGradedAnswerDto(question),
                getAnswerOptionContent(question, gradedAnswer),
                getSubmittedOptionContent(question, gradedAnswer),
                gradedAnswer.isCorrect()
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

    private static String getAnswerOptionContent(QuizQuestion question, GradedAnswer gradedAnswer) {
        List<QuizOption> options = question.getQuizOptions();
        int submittedOptionIndex = gradedAnswer.getSelectedOptionIndex();

        return options.get(submittedOptionIndex)
                      .getContent();
    }

    private static String getSubmittedOptionContent(QuizQuestion quizQuestion, GradedAnswer gradedAnswer) {
        List<QuizOption> quizOptions = quizQuestion.getQuizOptions();
        int submittedIndex = gradedAnswer.getSelectedOptionIndex();

        return quizOptions.get(submittedIndex)
                          .getContent();
    }


    private static QuizResponse.QuizQuestionResponse toQuizDto(QuizQuestion quizQuestion) {
        List<QuizOptionResponse> quizOptionResponses = quizQuestion.getQuizOptions()
                                                                   .stream()
                                                                   .map(QuizApplicationMapper::toQuestionDto)
                                                                   .toList();

        return new QuizResponse.QuizQuestionResponse(
                quizQuestion.getId(),
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestionContent(),
                quizQuestion.getQuestionContent(),
                quizOptionResponses,
                quizQuestion.getQuizAnswerOption().getWordId()
        );
    }

    private static QuizOptionResponse toQuestionDto(QuizOption quizOption) {
        return new QuizOptionResponse(quizOption.getId(), quizOption.getContent());
    }
}
