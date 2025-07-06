package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleTodayQuizResponseMapper {

    public static SimpleTodayQuizResponse toDto(SimpleTodayQuizDto simpleTodayQuizDto) {
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
