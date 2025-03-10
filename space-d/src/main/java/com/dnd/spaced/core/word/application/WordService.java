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
import com.dnd.spaced.core.word.domain.Category;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.PopularWord;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordPageRequest;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WordService {

    private final Clock clock;
    private final WordRepository wordRepository;
    private final PopularWordRepository popularWordRepository;
    private final ApplicationEventPublisher eventPublisher;

    public WordResponse read(Long wordId) {
        Word word = findWord(wordId);

        eventPublisher.publishEvent(new WordViewCountIncrementEvent(word.getId(), LocalDateTime.now(clock)));
        eventPublisher.publishEvent(new WordViewCountStatisticsEvent(word.getId(), LocalDateTime.now(clock)));

        return WordApplicationMapper.toDto(word);
    }

    public WordCollectionResponse readAllBy(ReadAllWordRequest request, Pageable pageable) {
        Category category = Category.findBy(request.categoryName())
                                    .orElse(null);
        WordPageRequest wordPageRequest = new WordPageRequest(pageable, request.lastWordName());
        List<Word> words = wordRepository.findAllBy(category, wordPageRequest);

        return WordApplicationMapper.toWordCollectionDto(words);
    }

    public WordCollectionResponse search(SearchWordRequest request, Pageable pageable) {
        WordSearchCondition wordSearchCondition = new WordSearchCondition(
                request.name(),
                Category.findBy(request.categoryName())
                        .orElse(null),
                request.pronunciation()
        );
        WordSearchPageRequest wordSearchPageRequest = new WordSearchPageRequest(pageable, request.lastWordName());

        List<Word> words = wordRepository.search(wordSearchCondition, wordSearchPageRequest);

        return WordApplicationMapper.toWordCollectionDto(words);
    }

    public PopularWordCollectionResponse readPopularWordsAll() {
        List<PopularWord> popularWords = popularWordRepository.findAllBy(LocalDateTime.now(clock));

        return WordApplicationMapper.toPopularWordCollectionDto(popularWords);
    }

    private Word findWord(Long wordId) {
        return wordRepository.findBy(wordId)
                             .orElseThrow(() -> new WordNotFoundException("지정한 ID에 해당하는 용어를 찾을 수 없습니다."));
    }
}
