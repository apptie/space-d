package com.dnd.spaced.core.word.domain.repository.dto.request;

import com.dnd.spaced.core.word.domain.Category;

public record WordSearchCondition(String name, Category category, String pronunciation) {
}
