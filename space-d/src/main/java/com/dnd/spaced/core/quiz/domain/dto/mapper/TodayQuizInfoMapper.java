package com.dnd.spaced.core.quiz.domain.dto.mapper;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.dto.TodayQuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.TodayQuizInfo.TodayQuizOptionInfo;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TodayQuizInfoMapper {

    public static TodayQuizInfo toDto(TodayQuiz todayQuiz, List<TodayQuizOption> todayQuizOptions) {
        TodayQuizQuestion todayQuizQuestion = todayQuiz.getTodayQuizQuestion();
        List<TodayQuizOptionInfo> todayQuizOptionInfos = todayQuizOptions.stream()
                                                         .map(
                                                                 option -> new TodayQuizOptionInfo(
                                                                         option.getId(),
                                                                         option.getWordId(), option.getContent(),
                                                                         option.getOptionOrder()
                                                         ))
                                                         .toList();

        return new TodayQuizInfo(
                todayQuiz.getId(),
                todayQuizQuestion.getQuizCategory(),
                todayQuizQuestion.getQuestion(),
                todayQuizQuestion.getQuestionContent(),
                todayQuizQuestion.getTodayQuizAnswerOption(),
                todayQuizOptionInfos
        );
    }
}
