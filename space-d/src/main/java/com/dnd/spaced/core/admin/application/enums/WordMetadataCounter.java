package com.dnd.spaced.core.admin.application.enums;

import com.dnd.spaced.core.admin.application.enums.exception.WordMetadataCounterNotFoundException;
import com.dnd.spaced.core.word.domain.Category;
import com.dnd.spaced.core.word.domain.WordMetadata;
import java.util.Arrays;
import java.util.function.Consumer;

public enum WordMetadataCounter {

    DEVELOP(Category.DEVELOP, WordMetadata::addDevelopWordCount),
    DESIGN(Category.DESIGN, WordMetadata::addDesignWordCount),
    BUSINESS(Category.BUSINESS, WordMetadata::addBusinessWordCount);

    private final Category category;
    private final Consumer<WordMetadata> counter;

    WordMetadataCounter(Category category, Consumer<WordMetadata> counter) {
        this.category = category;
        this.counter = counter;
    }

    public static void count(Category category, WordMetadata wordMetadata) {
        Arrays.stream(WordMetadataCounter.values())
              .filter(counter -> counter.category.equals(category))
              .findAny()
              .orElseThrow(() -> new WordMetadataCounterNotFoundException("용어 메타데이터에 변경사항을 반영하지 못했습니다."))
              .counter
              .accept(wordMetadata);
    }
}
