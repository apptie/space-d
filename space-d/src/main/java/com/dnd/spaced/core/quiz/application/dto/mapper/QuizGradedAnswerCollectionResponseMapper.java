package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse.QuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.global.mapper.Mapper;
import java.util.List;

@Mapper
public class QuizGradedAnswerCollectionResponseMapper {

    public QuizGradedAnswerCollectionResponse toCollectionResponse(List<QuizGradedAnswer> quizGradedAnswers) {
        if (quizGradedAnswers.isEmpty()) {
            return new QuizGradedAnswerCollectionResponse(List.of(), null);
        }

        List<QuizGradedAnswerResponse> responses = quizGradedAnswers.stream()
                                                                    .map(this::toResponse)
                                                                    .toList();

        return new QuizGradedAnswerCollectionResponse(responses, responses.get(responses.size() - 1).id());
    }

    private QuizGradedAnswerResponse toResponse(QuizGradedAnswer quizGradedAnswer) {
        QuizQuestion question = quizGradedAnswer.getQuizQuestion();

        return new QuizGradedAnswerResponse(
                quizGradedAnswer.getId(),
                quizGradedAnswer.getAccountId(),
                quizGradedAnswer.getQuizId(),
                toResponse(question),
                question.getQuizAnswerOption().getAnswerContent(),
                quizGradedAnswer.getSelectedContent(),
                quizGradedAnswer.isCorrect()
        );
    }

    private QuizGradedAnswerResponse.QuizQuestionResponse toResponse(QuizQuestion quizQuestion) {
        return new QuizGradedAnswerResponse.QuizQuestionResponse(
                quizQuestion.getId(),
                quizQuestion.getQuizCategory().getName(),
                quizQuestion.getQuestion(),
                quizQuestion.getPassage()
        );
    }
}
