package com.dnd.spaced.core.admin.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateWordExampleRequest(@NotBlank String content) {
}
