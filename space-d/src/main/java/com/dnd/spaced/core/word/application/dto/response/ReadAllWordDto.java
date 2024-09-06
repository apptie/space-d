package com.dnd.spaced.core.word.application.dto.response;

import com.dnd.spaced.core.word.domain.Word;

public record ReadAllWordDto(
        Long id,
        String name,
        String meaning,
        String category,
        long viewCount
) {

    public static ReadAllWordDto from(Word word) {
        return new ReadAllWordDto(
                word.getId(),
                word.getName(),
                word.getWordMeaning().getMeaning(),
                word.getCategory().getName(),
                word.getViewCount()
        );
    }
}
