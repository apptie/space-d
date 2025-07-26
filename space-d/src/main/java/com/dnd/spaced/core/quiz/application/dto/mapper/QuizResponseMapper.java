package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse.QuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse.QuizQuestionResponse.QuizOptionResponse;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto.QuizQuestionDto;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto.QuizQuestionDto.QuizOptionDto;
import com.dnd.spaced.global.mapper.Mapper;
import java.util.List;

@Mapper
public class QuizResponseMapper {

    public QuizResponse toResponse(QuizDto quiz) {
        List<QuizQuestionResponse> quizQuestionResponses = quiz.quizQuestions()
                                                               .stream()
                                                               .map(this::toQuizQuestionResponse)
                                                               .toList();

        return new QuizResponse(
                quiz.id(),
                quiz.accountId(),
                quizQuestionResponses
        );
    }

    private QuizQuestionResponse toQuizQuestionResponse(QuizQuestionDto quizQuestion) {
        List<QuizOptionResponse> quizOptionResponses = quizQuestion.quizOptions()
                                                                   .stream()
                                                                   .map(this::toQuizOptionResponse)
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

    private QuizOptionResponse toQuizOptionResponse(QuizOptionDto quizOption) {
        return new QuizOptionResponse(quizOption.id(), quizOption.wordId(), quizOption.content());
    }
}
