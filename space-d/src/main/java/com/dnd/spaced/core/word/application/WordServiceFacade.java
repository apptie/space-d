package com.dnd.spaced.core.word.application;

import com.dnd.spaced.core.word.application.dto.WordApplicationMapper;
import com.dnd.spaced.core.word.application.dto.request.ReadAllWordRequest;
import com.dnd.spaced.core.word.application.dto.request.SearchWordRequest;
import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse;
import com.dnd.spaced.core.word.application.event.dto.WordViewCountIncrementEvent;
import com.dnd.spaced.core.word.application.event.dto.WordViewCountStatisticsEvent;
import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.dto.PopularWord;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordServiceFacade {

    private final Clock clock;
    private final ReadWordViewService readWordViewService;
    private final SearchWordViewService searchWordViewService;
    private final ReadPopularWordService readPopularWordService;
    private final WordApplicationMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public WordResponse readWord(Long wordId) {
        WordView wordView = readWordView(wordId);

        publishWordViewCountIncrementedEvent(wordView);
        return mapper.toPronunciationResponse(wordView);
    }

    public WordCollectionResponse readWords(ReadAllWordRequest request, Pageable pageable) {
        List<WordView> words = readWordViewService.readWords(request, pageable);

        return mapper.toWordCollectionResponse(words);
    }

    public WordCollectionResponse searchWord(SearchWordRequest request, Pageable pageable) {
        List<WordView> words = searchWordViewService.searchWord(request, pageable);

        return mapper.toWordCollectionResponse(words);
    }

    public PopularWordCollectionResponse readPopularWords() {
        List<PopularWord> popularWords = readPopularWordService.readPopularWords();

        return mapper.toPopularWordCollectionResponse(popularWords);
    }

    private WordView readWordView(Long wordId) {
        return readWordViewService.readWord(wordId);
    }

    private void publishWordViewCountIncrementedEvent(WordView word) {
        eventPublisher.publishEvent(new WordViewCountIncrementEvent(word.id(), LocalDateTime.now(clock)));
        eventPublisher.publishEvent(new WordViewCountStatisticsEvent(word.id(), LocalDateTime.now(clock)));
    }
}
