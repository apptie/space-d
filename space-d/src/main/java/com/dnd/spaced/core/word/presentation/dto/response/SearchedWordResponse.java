package com.dnd.spaced.core.word.presentation.dto.response;

import com.dnd.spaced.core.word.application.dto.response.SearchedWordDto;
import java.util.List;

public record SearchedWordResponse(List<WordInfoResponse> words) {

    private record WordInfoResponse(Long id, String name, String meaning, String category, long viewCount) {
    }

    public static SearchedWordResponse from(List<SearchedWordDto> dtos) {
        List<WordInfoResponse> wordInfoResponses = dtos.stream()
                                                       .map(
                                                               dto -> new WordInfoResponse(
                                                                       dto.id(),
                                                                       dto.name(),
                                                                       dto.meaning(),
                                                                       dto.category(),
                                                                       dto.viewCount()
                                                               )
                                                       )
                                                       .toList();

        return new SearchedWordResponse(wordInfoResponses);
    }
}
