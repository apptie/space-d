package com.dnd.spaced.core.word.application.dto.request;

import jakarta.annotation.Nullable;

public record SearchWordRequest(
        @Nullable
        String name,

        @Nullable
        String categoryName,

        @Nullable
        String pronunciation,

        @Nullable
        String lastWordName
) {
}
