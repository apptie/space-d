package com.dnd.spaced.core.word.application.dto;

import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse.PopularWordResponse;
import com.dnd.spaced.core.word.application.dto.response.WordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse.PronunciationResponse;
import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.repository.dto.PopularWord;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WordApplicationMapper {

    public static WordCollectionResponse toWordCollectionDto(List<Word> words) {
        if (words.isEmpty()) {
            return new WordCollectionResponse(List.of(), null);
        }

        List<WordResponse> wordResponses = words.stream()
                                                .map(WordApplicationMapper::toDto)
                                                .toList();

        return new WordCollectionResponse(wordResponses, wordResponses.get(wordResponses.size() - 1).name());
    }

    public static WordResponse toDto(Word word) {
        List<PronunciationResponse> pronunciations = toDto(word.getPronunciations());
        List<String> examples = word.getWordExamples().stream()
                                    .map(WordExample::getExample)
                                    .toList();

        return new WordResponse(
                word.getId(),
                word.getName(),
                word.getCategory().getName(),
                word.getWordMeaning().getMeaning(),
                examples,
                pronunciations,
                word.getViewCount()
        );
    }

    public static PopularWordCollectionResponse toPopularWordCollectionDto(List<PopularWord> popularWords) {
        List<PopularWordResponse> responses = popularWords.stream()
                                                                     .map(
                                                                             popularWord -> new PopularWordResponse(
                                                                                     popularWord.rank(),
                                                                                     popularWord.wordId(),
                                                                                     popularWord.name()
                                                                             )
                                                                     )
                                                                     .toList();

        return new PopularWordCollectionResponse(responses);
    }

    private static List<PronunciationResponse> toDto(List<Pronunciation> pronunciations) {
        return pronunciations.stream()
                             .map(
                                     pronunciation -> new PronunciationResponse(
                                             pronunciation.getContent(),
                                             pronunciation.getType().getName()
                                     )
                             )
                             .toList();
    }
}
