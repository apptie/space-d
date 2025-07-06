package com.dnd.spaced.core.quiz.domain.dto.mapper;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import com.dnd.spaced.core.quiz.domain.dto.TodayQuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.TodayQuizInfo.TodayQuizOptionInfo;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TodayQuizInfoMapper {

    public static SimpleTodayQuizDto toDto(TodayQuiz todayQuiz) {
        TodayQuizQuestion todayQuizQuestion = todayQuiz.getTodayQuizQuestion();

        return new SimpleTodayQuizDto(
                todayQuiz.getId(),
                todayQuizQuestion.getQuizCategory(),
                todayQuizQuestion.getQuestion(),
                todayQuizQuestion.getPassage(),
                todayQuizQuestion.getTodayQuizAnswerOption(),
                todayQuiz.getCreatedAt()
        );
    }

    public static SimpleTodayQuizDto toDto(
            Long id,
            LocalDateTime createdAt,
            String question,
            String questionContent,
            QuizCategory quizCategory,
            String answerContent,
            Long answerWordId
    ) {
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(answerWordId, answerContent);

        return new SimpleTodayQuizDto(id, quizCategory, question, questionContent, todayQuizAnswerOption, createdAt);
    }

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
                todayQuizQuestion.getPassage(),
                todayQuizQuestion.getTodayQuizAnswerOption(),
                todayQuizOptionInfos
        );
    }
}
