package com.dnd.spaced.core.word.domain.dto;

import com.dnd.spaced.core.word.domain.embed.WordMeaning;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.enums.PronunciationType;
import java.util.List;

public record WordInfo(
        Long id,
        String name,
        Category category,
        WordMeaning wordMeaning,
        List<PronunciationInfo> pronunciations,
        List<WordExampleInfo> wordExamples,
        long viewCount,
        long bookmarkCount
) {

    public record PronunciationInfo(Long id, Long wordId, String content, PronunciationType type) {
    }

    public record WordExampleInfo(Long id, Long wordId, String example) {
    }
}
