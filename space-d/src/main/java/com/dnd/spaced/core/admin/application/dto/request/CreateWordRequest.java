package com.dnd.spaced.core.admin.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateWordRequest(
        @NotBlank
        String name,

        @NotBlank
        String meaning,

        @NotBlank
        String categoryName,

        @Size(min = 1)
        List<CreatePronunciationRequest> pronunciations,

        @Size(min = 1)
        List<String> examples
) {

    public record CreatePronunciationRequest(@NotBlank String pronunciation, @NotBlank String typeName) {
    }
}
