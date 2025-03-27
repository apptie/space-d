package com.dnd.spaced.core.skill.application.event.dto;

import java.time.LocalDateTime;

public record FailedGradedQuizSkillEvent(Long accountId, long correctCount, LocalDateTime failedAt) {
}
