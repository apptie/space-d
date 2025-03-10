package com.dnd.spaced.core.word.application.dto.response;

import java.util.List;

public record WordResponse(
        Long id,
        String name,
        String category,
        String meaning,
        List<String> examples,
        List<PronunciationResponse> pronunciations,
        long viewCount
) {

    public record PronunciationResponse(String pronunciation, String type) {
    }
}
