package com.dnd.spaced.core.word.presentation.dto.response;

import com.dnd.spaced.core.word.application.dto.response.ReadWordDto;
import java.util.List;

public record ReadWordResponse(
        Long id,
        String name,
        String categoryName,
        String meaning,
        List<String> examples,
        List<WordPronunciationInfoResponse> pronunciationInfo,
        long viewCount
) {

    public record WordPronunciationInfoResponse(String pronunciation, String typeName) {
    }

    public static ReadWordResponse from(ReadWordDto dto) {
        List<WordPronunciationInfoResponse> wordPronunciationInfo = dto.pronunciationInfo()
                                                                       .stream()
                                                                       .map(
                                                                               pronunciationInfo -> new WordPronunciationInfoResponse(
                                                                                       pronunciationInfo.pronunciation(),
                                                                                       pronunciationInfo.typeName()
                                                                               )
                                                                       )
                                                                       .toList();

        return new ReadWordResponse(
                dto.id(),
                dto.name(),
                dto.categoryName(),
                dto.meaning(),
                dto.examples(),
                wordPronunciationInfo,
                dto.viewCount()
        );
    }
}
