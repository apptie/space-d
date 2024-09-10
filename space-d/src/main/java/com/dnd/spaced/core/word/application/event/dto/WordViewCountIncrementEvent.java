package com.dnd.spaced.core.word.application.event.dto;

import java.time.LocalDateTime;

public record WordViewCountIncrementEvent(Long wordId, LocalDateTime localDateTime) {
}
