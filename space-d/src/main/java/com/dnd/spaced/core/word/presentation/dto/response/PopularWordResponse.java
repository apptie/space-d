package com.dnd.spaced.core.word.presentation.dto.response;

import com.dnd.spaced.core.word.application.dto.response.PopularWordDto;
import java.util.List;

public record PopularWordResponse(List<PopularWordInfoResponse> words) {

    public record PopularWordInfoResponse(int rank, Long id, String name) {
    }

    public static PopularWordResponse from(List<PopularWordDto> dtos) {
        List<PopularWordInfoResponse> popularWordInfo = dtos.stream()
                                                            .map(dto ->
                                                                    new PopularWordInfoResponse(
                                                                            dto.rank(),
                                                                            dto.wordId(),
                                                                            dto.name()
                                                                    )
                                                            )
                                                            .toList();

        return new PopularWordResponse(popularWordInfo);
    }
}
