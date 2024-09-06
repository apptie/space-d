package com.dnd.spaced.core.word.application.dto.request;

import org.springframework.data.domain.Pageable;

public record SearchConditionDto(
        String name,
        String categoryName,
        String pronunciation,
        Pageable pageable,
        String lastWordName
) {
}
