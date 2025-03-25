package com.dnd.spaced.core.word.application.event.dto;

import java.time.LocalDateTime;

public record FailedWordPersistedEvent(Long wordId, String categoryName, LocalDateTime failedAt) {
}
