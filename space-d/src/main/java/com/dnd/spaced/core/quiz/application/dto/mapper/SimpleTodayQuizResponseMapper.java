package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleTodayQuizResponseMapper {

    public static SimpleTodayQuizResponse toDto(SimpleTodayQuizInfo simpleTodayQuizInfo) {
        TodayQuizQuestionResponse todayQuizQuestionResponse = new TodayQuizQuestionResponse(
                simpleTodayQuizInfo.quizCategory().getName(),
                simpleTodayQuizInfo.question(),
                simpleTodayQuizInfo.questionContent()
        );

        return new SimpleTodayQuizResponse(
                simpleTodayQuizInfo.id(),
                todayQuizQuestionResponse,
                simpleTodayQuizInfo.createdAt()
        );
    }
}
