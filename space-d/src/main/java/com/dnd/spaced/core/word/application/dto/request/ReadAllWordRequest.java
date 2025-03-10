package com.dnd.spaced.core.word.application.dto.request;

import jakarta.annotation.Nullable;

public record ReadAllWordRequest(@Nullable String category, @Nullable String lastWordName) {
}
