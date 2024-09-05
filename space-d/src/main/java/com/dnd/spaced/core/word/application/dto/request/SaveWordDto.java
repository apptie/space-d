package com.dnd.spaced.core.word.application.dto.request;

import java.util.List;

public record SaveWordDto(
        String name,
        String meaning,
        String categoryName,
        List<PronunciationInfoDto> pronunciations,
        List<String> examples
) {

    public record PronunciationInfoDto(String pronunciation, String typeName) {
    }
}
