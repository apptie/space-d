package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.exception.InvalidCategoryNameException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Category {
    BUSINESS("비즈니스"), DEVELOP("개발"), DESIGN("디자인");

    private static final String EXCEPTION_FORMAT = "잘못된 카테고리 이름 '%s'를 입력했습니다.";

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public static Category findBy(String name) {
        return Arrays.stream(Category.values())
                     .filter(category -> category.name.equals(name))
                     .findAny()
                     .orElseThrow(() -> new InvalidCategoryNameException(String.format(EXCEPTION_FORMAT, name)));
    }
}
