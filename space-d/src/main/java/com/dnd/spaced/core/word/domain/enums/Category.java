package com.dnd.spaced.core.word.domain.enums;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum Category {

    BUSINESS("비즈니스"),
    DEVELOP("개발"),
    DESIGN("디자인");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public static Optional<Category> findBy(String name) {
        return Arrays.stream(Category.values())
                     .filter(category -> category.name.equals(name))
                     .findAny();
    }
}
