package com.dnd.spaced.core.quiz.domain.dto.mapper;

import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo.QuizQuestionInfo;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo.QuizQuestionInfo.QuizOptionInfo;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizInfoMapper {

    public static QuizInfo toDto(Quiz quiz) {
        return new QuizInfo(
                quiz.getId(),
                quiz.getAccountId(),
                quiz.isSolved(),
                quiz.getCreatedAt(),
                quiz.getUpdatedAt(),
                Collections.emptyList()
        );
    }

    public static QuizInfo toDto(Quiz quiz, Map<Long, List<QuizOption>> quizOptionMap) {
        List<QuizQuestionInfo> quizQuestions = quiz.getQuizQuestions()
                                          .stream()
                                          .map(quizQuestion -> toQuizQuestionDto(quizQuestion, quizOptionMap.get(quizQuestion.getId())))
                                          .toList();

        return new QuizInfo(
                quiz.getId(),
                quiz.getAccountId(),
                quiz.isSolved(),
                quiz.getCreatedAt(),
                quiz.getUpdatedAt(),
                quizQuestions
        );
    }

    private static QuizQuestionInfo toQuizQuestionDto(QuizQuestion quizQuestion, List<QuizOption> quizOptions) {
        if (quizOptions == null) {
            return new QuizQuestionInfo(
                    quizQuestion.getId(),
                    quizQuestion.getQuizCategory(),
                    quizQuestion.getQuizAnswerOption(),
                    quizQuestion.getQuestionContent(),
                    quizQuestion.getQuestionExample(),
                    Collections.emptyList()
            );
        }
        List<QuizOptionInfo> quizOptionInfos = quizOptions.stream()
                                                          .map(QuizInfoMapper::toQuizOptionDto)
                                                          .toList();

        return new QuizQuestionInfo(
                quizQuestion.getId(),
                quizQuestion.getQuizCategory(),
                quizQuestion.getQuizAnswerOption(),
                quizQuestion.getQuestionContent(),
                quizQuestion.getQuestionExample(),
                quizOptionInfos
        );
    }

    private static QuizOptionInfo toQuizOptionDto(QuizOption quizOption) {
        return new QuizOptionInfo(
                quizOption.getId(),
                quizOption.getWordId(),
                quizOption.getContent(),
                quizOption.getOptionOrder()
        );
    }
}
