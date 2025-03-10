package com.dnd.spaced.core.comment.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(@NotBlank String content) {
}
