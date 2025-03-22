package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse.QuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizGradedAnswerCollectionResponseMapper {

    public static QuizGradedAnswerCollectionResponse toCollectionDto(List<QuizGradedAnswer> quizGradedAnswers) {
        if (quizGradedAnswers.isEmpty()) {
            return new QuizGradedAnswerCollectionResponse(List.of(), null);
        }

        List<QuizGradedAnswerResponse> responses = quizGradedAnswers.stream()
                                                                    .map(QuizGradedAnswerCollectionResponseMapper::toDto)
                                                                    .toList();

        return new QuizGradedAnswerCollectionResponse(responses, responses.get(responses.size() - 1).id());
    }

    private static QuizGradedAnswerResponse toDto(QuizGradedAnswer quizGradedAnswer) {
        QuizQuestion question = quizGradedAnswer.getQuizQuestion();

        return new QuizGradedAnswerResponse(
                quizGradedAnswer.getId(),
                quizGradedAnswer.getAccountId(),
                quizGradedAnswer.getQuizId(),
                toDto(question),
                question.getQuizAnswerOption().getAnswerContent(),
                quizGradedAnswer.getSelectedContent(),
                quizGradedAnswer.isCorrect()
        );
    }

    private static QuizGradedAnswerResponse.QuizQuestionResponse toDto(QuizQuestion quizQuestion) {
        return new QuizGradedAnswerResponse.QuizQuestionResponse(
                quizQuestion.getId(),
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestion(),
                quizQuestion.getPassage()
        );
    }
}
