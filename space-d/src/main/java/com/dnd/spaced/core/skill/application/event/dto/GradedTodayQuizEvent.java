package com.dnd.spaced.core.skill.application.event.dto;

public record GradedTodayQuizEvent(Long accountId, boolean corrected) {
}
