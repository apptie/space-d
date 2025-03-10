package com.dnd.spaced.core.word.application.dto.response;

import java.util.List;

public record PopularWordCollectionResponse(List<PopularWordResponse> popularWords) {

    public record PopularWordResponse(int rank, Long wordId, String name) {
    }
}
