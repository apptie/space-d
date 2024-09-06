package com.dnd.spaced.core.word.application.dto.response;

import com.dnd.spaced.core.word.domain.Word;

public record SearchedWordDto(
        Long id,
        String name,
        String meaning,
        String category,
        long viewCount
) {

    public static SearchedWordDto from(Word word) {
        return new SearchedWordDto(
                word.getId(),
                word.getName(),
                word.getWordMeaning().getMeaning(),
                word.getCategory().getName(),
                word.getViewCount()
        );
    }
}
