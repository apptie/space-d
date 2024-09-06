package com.dnd.spaced.core.admin.presentation.dto.request;

import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto.PronunciationInfoDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SaveWordRequest(
        @NotBlank
        String name,

        @NotBlank
        String meaning,

        @NotBlank
        String categoryName,

        @NotEmpty
        List<PronunciationInfoRequest> pronunciations,

        @NotEmpty
        List<String> examples
) {

    public SaveWordDto to() {
        List<PronunciationInfoDto> pronunciationInfoDtos = pronunciations.stream()
                                                                         .map(
                                                                                 pronunciation -> new PronunciationInfoDto(
                                                                                         pronunciation.pronunciation,
                                                                                         pronunciation.typeName)
                                                                         )
                                                                         .toList();

        return new SaveWordDto(
                name,
                meaning,
                categoryName,
                pronunciationInfoDtos,
                examples
        );
    }

    public record PronunciationInfoRequest(String pronunciation, String typeName) {
    }
}
