package com.dnd.spaced.core.quiz.application.enums;

import com.dnd.spaced.core.quiz.application.enums.exception.QuizCategoryNotFoundException;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.word.domain.WordMetadata;
import java.util.Arrays;
import java.util.function.BiPredicate;

public enum QuizWordCountValidator {

    DEVELOP(QuizCategory.DEVELOP, WordMetadata::canGenerateBusinessQuiz),
    DESIGN(QuizCategory.DESIGN, WordMetadata::canGenerateDesignQuiz),
    BUSINESS(QuizCategory.BUSINESS, WordMetadata::canGenerateDesignQuiz),
    TOTAL(QuizCategory.TOTAL, WordMetadata::canGenerateTotalQuiz);

    private final QuizCategory category;
    private final BiPredicate<WordMetadata, Integer> validator;

    QuizWordCountValidator(QuizCategory quizCategory, BiPredicate<WordMetadata, Integer> validator) {
        this.category = quizCategory;
        this.validator = validator;
    }

    public static boolean isValidate(QuizCategory category, WordMetadata wordMetadata, int requiredWordCount) {
        return Arrays.stream(QuizWordCountValidator.values())
                     .filter(validator -> validator.category.equals(category))
                     .findAny()
                     .orElseThrow(() -> new QuizCategoryNotFoundException("지정한 퀴즈 카테고리를 찾을 수 없습니다."))
                     .validator
                     .test(wordMetadata, requiredWordCount);
    }

    public static boolean isBlocked(QuizCategory category, WordMetadata wordMetadata, int requiredWordCount) {
        return !isValidate(category, wordMetadata, requiredWordCount);
    }
}
