package com.dnd.spaced.core.bookmark.application.dto.request;

import jakarta.validation.constraints.Positive;

public record CreateBookmarkRequest(@Positive Long wordId) {
}
