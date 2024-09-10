package com.dnd.spaced.core.word.application.event.dto;

import java.time.LocalDateTime;

public record WordViewCountStatisticsEvent(Long wordId, LocalDateTime localDateTime) {
}
