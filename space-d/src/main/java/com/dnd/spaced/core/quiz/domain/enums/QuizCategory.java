package com.dnd.spaced.core.quiz.domain.enums;

import com.dnd.spaced.core.quiz.domain.enums.exception.InvalidQuizCategoryNameException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;

@Getter
public enum QuizCategory {

    BUSINESS("비즈니스"),
    DEVELOP("개발"),
    DESIGN("디자인"),
    TOTAL("전체 실무");

    private final String name;

    private static final String EXCEPTION_FORMAT = "잘못된 퀴즈 카테고리 이름 '%s'을(를) 입력했습니다.";

    QuizCategory(String name) {
        this.name = name;
    }

    public static QuizCategory findBy(String name) {
        return Arrays.stream(QuizCategory.values())
                     .filter(category -> category.name.equals(name))
                     .findAny()
                     .orElseThrow(() -> new InvalidQuizCategoryNameException(String.format(EXCEPTION_FORMAT, name)));
    }

    public static QuizCategory findRandom() {
        QuizCategory[] quizCategories = QuizCategory.values();

        return quizCategories[ThreadLocalRandom.current().nextInt(quizCategories.length)];
    }

    public boolean isTotal() {
        return this == QuizCategory.TOTAL;
    }

    public boolean isNotTotal() {
        return !isTotal();
    }
}
