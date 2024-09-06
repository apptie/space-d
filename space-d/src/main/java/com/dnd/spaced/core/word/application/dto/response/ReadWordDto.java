package com.dnd.spaced.core.word.application.dto.response;

import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
import java.util.List;

public record ReadWordDto(
        Long id,
        String name,
        String categoryName,
        String meaning,
        List<String> examples,
        List<WordPronunciationInfoDto> pronunciationInfo,
        long viewCount
) {

    public record WordPronunciationInfoDto(String pronunciation, String typeName) {

    }

    public static ReadWordDto from(Word word) {
        List<WordPronunciationInfoDto> wordPronunciationInfo = word.getPronunciations()
                                                                   .stream()
                                                                   .map(pronunciation ->
                                                                           new WordPronunciationInfoDto(
                                                                                   pronunciation.getContent(),
                                                                                   pronunciation.getType().getName()
                                                                           )
                                                                   )
                                                                   .toList();
        List<String> examples = word.getWordExamples()
                                   .stream()
                                   .map(WordExample::getExample)
                                   .toList();

        return new ReadWordDto(
                word.getId(),
                word.getName(),
                word.getCategory().getName(),
                word.getWordMeaning().getMeaning(),
                examples,
                wordPronunciationInfo,
                word.getViewCount()
        );
    }
}
