package com.dnd.spaced.core.admin.application.dto.resposne;

import com.dnd.spaced.core.word.domain.enums.Category;

public record PersistWordDto(Long id, Category category) {
}
