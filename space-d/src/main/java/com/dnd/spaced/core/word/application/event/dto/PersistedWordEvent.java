package com.dnd.spaced.core.word.application.event.dto;

import com.dnd.spaced.core.word.domain.enums.Category;

public record PersistedWordEvent(Long wordId, Category category) {
}
