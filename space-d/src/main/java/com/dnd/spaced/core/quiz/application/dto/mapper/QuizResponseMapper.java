package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse.QuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse.QuizQuestionResponse.QuizOptionResponse;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto.QuizQuestionDto;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto.QuizQuestionDto.QuizOptionDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizResponseMapper {

    public static QuizResponse toDto(QuizDto quiz) {
        List<QuizQuestionResponse> quizQuestionResponses = quiz.quizQuestions()
                                                               .stream()
                                                               .map(QuizResponseMapper::toQuizDto)
                                                               .toList();

        return new QuizResponse(
                quiz.id(),
                quiz.accountId(),
                quizQuestionResponses
        );
    }

    private static QuizQuestionResponse toQuizDto(QuizQuestionDto quizQuestion) {
        List<QuizOptionResponse> quizOptionResponses = quizQuestion.quizOptions()
                                                                   .stream()
                                                                   .map(QuizResponseMapper::toQuizOptionDto)
                                                                   .toList();

        return new QuizQuestionResponse(
                quizQuestion.id(),
                quizQuestion.quizCategory().getName(),
                quizQuestion.questionContent(),
                quizQuestion.questionExample(),
                quizOptionResponses,
                quizQuestion.quizAnswerOption().getAnswerWordId()
        );
    }

    private static QuizOptionResponse toQuizOptionDto(QuizOptionDto quizOption) {
        return new QuizOptionResponse(quizOption.id(), quizOption.wordId(), quizOption.content());
    }
}
