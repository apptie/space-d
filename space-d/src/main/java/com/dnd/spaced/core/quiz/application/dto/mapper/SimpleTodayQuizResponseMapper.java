package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import com.dnd.spaced.global.mapper.Mapper;

@Mapper
public class SimpleTodayQuizResponseMapper {

    public SimpleTodayQuizResponse toResponse(SimpleTodayQuizDto simpleTodayQuizDto) {
        TodayQuizQuestionResponse todayQuizQuestionResponse = new TodayQuizQuestionResponse(
                simpleTodayQuizDto.quizCategory().getName(),
                simpleTodayQuizDto.question(),
                simpleTodayQuizDto.questionContent()
        );

        return new SimpleTodayQuizResponse(
                simpleTodayQuizDto.id(),
                todayQuizQuestionResponse,
                simpleTodayQuizDto.createdAt()
        );
    }
}
