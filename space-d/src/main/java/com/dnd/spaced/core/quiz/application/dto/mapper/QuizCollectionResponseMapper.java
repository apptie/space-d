package com.dnd.spaced.core.quiz.application.dto.mapper;

import static com.dnd.spaced.core.quiz.application.dto.response.QuizCollectionResponse.*;
import static com.dnd.spaced.core.quiz.domain.dto.SimpleQuizInfo.*;

import com.dnd.spaced.core.quiz.application.dto.response.QuizCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizCollectionResponse.QuizResponse.QuizQuestionResponse;
import com.dnd.spaced.core.quiz.domain.dto.SimpleQuizInfo;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizCollectionResponseMapper {

    public static QuizCollectionResponse toCollectionResponse(List<SimpleQuizInfo> quizzes) {
        if (quizzes == null || quizzes.isEmpty()) {
            return new QuizCollectionResponse(
                    Collections.emptyList(),
                    null
            );
        }

        List<QuizResponse> quizResponses = quizzes.stream()
                                              .map(QuizCollectionResponseMapper::toQuizResponse)
                                              .toList();

        return new QuizCollectionResponse(quizResponses, quizResponses.get(quizResponses.size() - 1).id());
    }

    private static QuizResponse toQuizResponse(SimpleQuizInfo quiz) {
        return new QuizResponse(
                quiz.id(),
                quiz.accountId(),
                quiz.solved(),
                quiz.createdAt(),
                toQuizQuestionResponse(quiz.quizQuestions())
        );
    }

    private static List<QuizQuestionResponse> toQuizQuestionResponse(List<QuizQuestionInfo> quizQuestions) {
        return quizQuestions.stream()
                            .map(
                                    quizQuestion ->
                                            new QuizQuestionResponse(
                                                    quizQuestion.quizCategory().getName(),
                                                    quizQuestion.questionExample()
                                            )
                            )
                            .toList();
    }
}
