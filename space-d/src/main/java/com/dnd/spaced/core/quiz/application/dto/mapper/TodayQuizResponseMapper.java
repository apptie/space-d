package com.dnd.spaced.core.quiz.application.dto.mapper;

import com.dnd.spaced.core.quiz.application.dto.response.ReadTodayQuizDto;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse.TodayQuizOptionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizStatus;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.global.consts.AuthConst;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TodayQuizResponseMapper {

    public static TodayQuizResponse toDto(ReadTodayQuizDto todayQuizDto, Long accountId) {
        TodayQuizQuestionResponse todayQuizQuestion = toTodayQuizQuestionDto(
                todayQuizDto.todayQuiz().getTodayQuizQuestion()
        );

        if (AuthConst.GUEST_ACCOUNT_ID.equals(accountId)) {
            return new TodayQuizResponse(
                    todayQuizDto.todayQuiz().getId(),
                    todayQuizQuestion,
                    TodayQuizStatus.NOT_LOGGED_IN
            );
        }
        if (todayQuizDto.solved()) {
            return new TodayQuizResponse(todayQuizDto.todayQuiz().getId(), todayQuizQuestion, TodayQuizStatus.SOLVED);
        }
        return new TodayQuizResponse(todayQuizDto.todayQuiz().getId(), todayQuizQuestion, TodayQuizStatus.NOT_SOLVED);
    }

    private static TodayQuizQuestionResponse toTodayQuizQuestionDto(TodayQuizQuestion todayQuizQuestion) {
        List<TodayQuizOptionResponse> todayQuizOptionResponses = toTodayQuizOptionDto(todayQuizQuestion);

        return new TodayQuizQuestionResponse(
                todayQuizQuestion.getQuizCategory().getName(),
                todayQuizQuestion.getQuestion(),
                todayQuizQuestion.getPassage(),
                todayQuizOptionResponses,
                todayQuizQuestion.getTodayQuizAnswerOption().getAnswerWordId(),
                todayQuizQuestion.getTodayQuizAnswerOption().getAnswerContent()
        );
    }

    private static List<TodayQuizOptionResponse> toTodayQuizOptionDto(TodayQuizQuestion todayQuizQuestion) {
        return todayQuizQuestion.getTodayQuizOptions()
                                .stream()
                                .map(todayQuizOption ->
                                        new TodayQuizOptionResponse(
                                                todayQuizOption.getId(),
                                                todayQuizOption.getWordId(),
                                                todayQuizOption.getContent(),
                                                todayQuizOption.getOptionOrder()
                                        )
                                )
                                .toList();
    }
}
