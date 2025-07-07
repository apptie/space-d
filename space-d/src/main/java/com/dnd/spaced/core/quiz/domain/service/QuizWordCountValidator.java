package com.dnd.spaced.core.quiz.domain.service;

import com.dnd.spaced.core.quiz.application.exception.QuizCategoryNotFoundException;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.word.domain.WordMetadata;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiPredicate;

public class QuizWordCountValidator {

    private final Map<QuizCategory, BiPredicate<WordMetadata, Integer>> validators;

    public static QuizWordCountValidator create() {
        Map<QuizCategory, BiPredicate<WordMetadata, Integer>> validators = createValidators();

        return new QuizWordCountValidator(validators);
    }

    private static Map<QuizCategory, BiPredicate<WordMetadata, Integer>> createValidators() {
        Map<QuizCategory, BiPredicate<WordMetadata, Integer>> validators = new EnumMap<>(QuizCategory.class);

        validators.put(QuizCategory.BUSINESS, WordMetadata::canGenerateBusinessQuiz);
        validators.put(QuizCategory.DESIGN, WordMetadata::canGenerateDesignQuiz);
        validators.put(QuizCategory.DEVELOP, WordMetadata::canGenerateDevelopQuiz);
        validators.put(QuizCategory.TOTAL, WordMetadata::canGenerateTotalQuiz);
        return validators;
    }

    private QuizWordCountValidator(Map<QuizCategory, BiPredicate<WordMetadata, Integer>> validators) {
        this.validators = validators;
    }

    public boolean isValidate(QuizCategory category, WordMetadata wordMetadata, int requiredWordCount) {
        BiPredicate<WordMetadata, Integer> validator = validators.get(category);

        if (validator == null) {
            throw new QuizCategoryNotFoundException("지정한 퀴즈 카테고리를 찾을 수 없습니다.");
        }

        return validator.test(wordMetadata, requiredWordCount);
    }

    public boolean isInvalidate(QuizCategory category, WordMetadata wordMetadata, int requiredWordCount) {
        return !isValidate(category, wordMetadata, requiredWordCount);
    }
}
