package com.dnd.spaced.core.word.application;

import com.dnd.spaced.core.word.application.dto.WordApplicationMapper;
import com.dnd.spaced.core.word.application.dto.request.ReadAllWordRequest;
import com.dnd.spaced.core.word.application.dto.request.SearchWordRequest;
import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse;
import com.dnd.spaced.core.word.application.event.dto.WordViewCountIncrementEvent;
import com.dnd.spaced.core.word.application.event.dto.WordViewCountStatisticsEvent;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.WordViewRepository;
import com.dnd.spaced.core.word.domain.dto.PopularWord;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordService {

    private final Clock clock;
    private final WordViewRepository wordViewRepository;
    private final PopularWordRepository popularWordRepository;
    private final WordApplicationMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public WordResponse readWord(Long wordId) {
        WordView word = findWord(wordId);

        publishWordViewCountIncrementedEvent(word);
        return mapper.toPronunciationResponse(word);
    }

    public WordCollectionResponse readWords(ReadAllWordRequest request, Pageable pageable) {
        Category category = Category.findBy(request.categoryName())
                                    .orElse(null);
        Category lastCategory = Category.findBy(request.lastCategoryName())
                                        .orElse(null);
        List<WordView> words = wordViewRepository.findAllBy(category, request.lastWordName(), lastCategory, pageable);

        return mapper.toWordCollectionResponse(words);
    }

    public WordCollectionResponse searchWord(SearchWordRequest request, Pageable pageable) {
        Category category = Category.findBy(request.categoryName())
                                    .orElse(null);
        Category lastCategory = Category.findBy(request.lastCategoryName())
                                        .orElse(null);
        WordSearchCondition wordSearchCondition = new WordSearchCondition(
                request.name(),
                category,
                request.pronunciation()
        );
        WordSearchPageRequest wordSearchPageRequest = new WordSearchPageRequest(
                pageable,
                request.lastWordName(),
                lastCategory);
        List<WordView> words = wordViewRepository.search(wordSearchCondition, wordSearchPageRequest);

        return mapper.toWordCollectionResponse(words);
    }

    public PopularWordCollectionResponse readPopularWords() {
        List<PopularWord> popularWords = popularWordRepository.findAllBy(LocalDateTime.now(clock));

        return mapper.toPopularWordCollectionResponse(popularWords);
    }

    private WordView findWord(Long wordId) {
        return wordViewRepository.findBy(wordId)
                                 .orElseThrow(() -> new WordNotFoundException("지정한 ID에 해당하는 용어를 찾을 수 없습니다."));
    }

    private void publishWordViewCountIncrementedEvent(WordView word) {
        eventPublisher.publishEvent(new WordViewCountIncrementEvent(word.id(), LocalDateTime.now(clock)));
        eventPublisher.publishEvent(new WordViewCountStatisticsEvent(word.id(), LocalDateTime.now(clock)));
    }
}
