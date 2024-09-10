package com.dnd.spaced.core.word.application.dto.response;

import com.dnd.spaced.core.word.domain.repository.dto.PopularWordInfo;

public record PopularWordDto(int rank, Long wordId, String name) {

    public static PopularWordDto from(PopularWordInfo popularWordInfo) {
        return new PopularWordDto(popularWordInfo.rank(), popularWordInfo.wordId(), popularWordInfo.name());
    }
}
