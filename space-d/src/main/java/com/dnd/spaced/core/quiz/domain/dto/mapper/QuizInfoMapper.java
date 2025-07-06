package com.dnd.spaced.core.quiz.domain.dto.mapper;

import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto.QuizQuestionDto;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto.QuizQuestionDto.QuizOptionDto;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizInfoMapper {

    public static QuizDto toDto(Quiz quiz) {
        return new QuizDto(
                quiz.getId(),
                quiz.getAccountId(),
                quiz.isSolved(),
                quiz.getCreatedAt(),
                Collections.emptyList()
        );
    }

    public static QuizDto toDto(Quiz quiz, Map<Long, List<QuizOption>> quizOptionMap) {
        List<QuizQuestionDto> quizQuestions = quiz.getQuizQuestions()
                                                  .stream()
                                                  .map(quizQuestion -> toQuizQuestionDto(quizQuestion, quizOptionMap.get(quizQuestion.getId())))
                                                  .toList();

        return new QuizDto(
                quiz.getId(),
                quiz.getAccountId(),
                quiz.isSolved(),
                quiz.getCreatedAt(),
                quizQuestions
        );
    }

    private static QuizQuestionDto toQuizQuestionDto(QuizQuestion quizQuestion, List<QuizOption> quizOptions) {
        if (quizOptions == null) {
            return new QuizQuestionDto(
                    quizQuestion.getId(),
                    quizQuestion.getQuizCategory(),
                    quizQuestion.getQuizAnswerOption(),
                    quizQuestion.getQuestion(),
                    quizQuestion.getPassage(),
                    Collections.emptyList()
            );
        }
        List<QuizOptionDto> quizOptionDtos = quizOptions.stream()
                                                        .map(QuizInfoMapper::toQuizOptionDto)
                                                        .toList();

        return new QuizQuestionDto(
                quizQuestion.getId(),
                quizQuestion.getQuizCategory(),
                quizQuestion.getQuizAnswerOption(),
                quizQuestion.getQuestion(),
                quizQuestion.getPassage(),
                quizOptionDtos
        );
    }

    private static QuizOptionDto toQuizOptionDto(QuizOption quizOption) {
        return new QuizOptionDto(
                quizOption.getId(),
                quizOption.getWordId(),
                quizOption.getContent(),
                quizOption.getOptionOrder()
        );
    }
}
