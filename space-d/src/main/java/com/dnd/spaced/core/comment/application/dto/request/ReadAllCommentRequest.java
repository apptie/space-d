package com.dnd.spaced.core.comment.application.dto.request;

import jakarta.validation.constraints.Positive;

public record ReadAllCommentRequest(@Positive Long lastCommentId) {
}
