package com.dnd.spaced.core.quiz.domain.dto.mapper;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import com.dnd.spaced.core.quiz.domain.dto.TodayQuizDto;
import com.dnd.spaced.core.quiz.domain.dto.TodayQuizDto.TodayQuizOptionDto;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.global.mapper.Mapper;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public class TodayQuizDtoMapper {

    public SimpleTodayQuizDto toDto(TodayQuiz todayQuiz) {
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

    public SimpleTodayQuizDto toDto(
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

    public TodayQuizDto toDto(TodayQuiz todayQuiz, List<TodayQuizOption> todayQuizOptions) {
        TodayQuizQuestion todayQuizQuestion = todayQuiz.getTodayQuizQuestion();
        List<TodayQuizOptionDto> todayQuizOptionDtos = todayQuizOptions.stream()
                                                                       .map(
                                                                               option -> new TodayQuizOptionDto(
                                                                                       option.getId(),
                                                                                       option.getWordId(),
                                                                                       option.getContent(),
                                                                                       option.getOptionOrder()
                                                                               )
                                                                       )
                                                                       .toList();

        return new TodayQuizDto(
                todayQuiz.getId(),
                todayQuizQuestion.getQuizCategory(),
                todayQuizQuestion.getQuestion(),
                todayQuizQuestion.getPassage(),
                todayQuizQuestion.getTodayQuizAnswerOption(),
                todayQuizOptionDtos
        );
    }
}
