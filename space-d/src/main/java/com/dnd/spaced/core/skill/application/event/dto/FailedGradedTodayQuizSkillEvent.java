package com.dnd.spaced.core.skill.application.event.dto;

import java.time.LocalDateTime;

public record FailedGradedTodayQuizSkillEvent(Long accountId, LocalDateTime failedAt) {
}
