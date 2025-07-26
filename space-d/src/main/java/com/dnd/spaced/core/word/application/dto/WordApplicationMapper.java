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
import com.dnd.spaced.global.mapper.Mapper;
import java.util.List;

@Mapper
public final class WordApplicationMapper {

    public WordCollectionResponse toWordCollectionResponse(List<WordView> words) {
        if (words.isEmpty()) {
            return new WordCollectionResponse(List.of(), null);
        }

        List<WordResponse> wordResponses = words.stream()
                                                .map(this::toPronunciationResponse)
                                                .toList();

        return new WordCollectionResponse(wordResponses, wordResponses.get(wordResponses.size() - 1).name());
    }

    public WordResponse toPronunciationResponse(WordView word) {
        List<PronunciationResponse> pronunciations = toPronunciationResponse(word.pronunciations());
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

    public PopularWordCollectionResponse toPopularWordCollectionResponse(List<PopularWord> popularWords) {
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

    private List<PronunciationResponse> toPronunciationResponse(List<PronunciationView> pronunciations) {
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
