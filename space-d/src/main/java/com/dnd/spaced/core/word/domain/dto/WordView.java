package com.dnd.spaced.core.word.domain.dto;

import com.dnd.spaced.core.word.domain.embed.WordMeaning;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.enums.PronunciationType;
import java.util.List;

public record WordView(
        Long id,
        String name,
        Category category,
        WordMeaning wordMeaning,
        List<PronunciationView> pronunciations,
        List<WordExampleView> wordExamples,
        long viewCount,
        long bookmarkCount
) {

    public record PronunciationView(Long id, Long wordId, String content, PronunciationType type) {
    }

    public record WordExampleView(Long id, Long wordId, String example) {
    }
}
