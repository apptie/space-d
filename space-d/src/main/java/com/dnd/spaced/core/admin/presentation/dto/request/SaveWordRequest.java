package com.dnd.spaced.core.admin.presentation.dto.request;

import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto.PronunciationInfoDto;
import java.util.List;

public record SaveWordRequest(
        String name,
        String meaning,
        String categoryName,
        List<PronunciationInfoRequest> pronunciations,
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
