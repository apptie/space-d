package com.dnd.spaced.core.word.application.dto;

import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse.PopularWordResponse;
import com.dnd.spaced.core.word.application.dto.response.WordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse.PronunciationResponse;
import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.dto.WordView.PronunciationView;
import com.dnd.spaced.core.word.domain.dto.WordView.WordExampleView;
import com.dnd.spaced.core.word.domain.dto.PopularWord;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WordApplicationMapper {

    public static WordCollectionResponse toWordCollectionDto(List<WordView> words) {
        if (words.isEmpty()) {
            return new WordCollectionResponse(List.of(), null);
        }

        List<WordResponse> wordResponses = words.stream()
                                                .map(WordApplicationMapper::toPronunciationInfoDto)
                                                .toList();

        return new WordCollectionResponse(wordResponses, wordResponses.get(wordResponses.size() - 1).name());
    }

    public static WordResponse toPronunciationInfoDto(WordView word) {
        List<PronunciationResponse> pronunciations = toPronunciationInfoDto(word.pronunciations());
        List<String> examples = word.wordExamples()
                                    .stream()
                                    .map(WordExampleView::example)
                                    .toList();

        return new WordResponse(
                word.id(),
                word.name(),
                word.category().getName(),
                word.wordMeaning().getMeaning(),
                examples,
                pronunciations,
                word.viewCount(),
                word.bookmarkCount()
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

    private static List<PronunciationResponse> toPronunciationInfoDto(List<PronunciationView> pronunciations) {
        return pronunciations.stream()
                             .map(
                                     pronunciation -> new PronunciationResponse(
                                             pronunciation.content(),
                                             pronunciation.type().getName()
                                     )
                             )
                             .toList();
    }
}
