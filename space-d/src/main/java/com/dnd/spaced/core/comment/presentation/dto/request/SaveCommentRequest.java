package com.dnd.spaced.core.comment.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record SaveCommentRequest(@NotEmpty String content) {
}
