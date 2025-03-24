package com.dnd.spaced.core.bookmark.application.dto.request;

import jakarta.validation.constraints.Positive;

public record DeleteBookmarkRequest(@Positive Long wordId) {
}
