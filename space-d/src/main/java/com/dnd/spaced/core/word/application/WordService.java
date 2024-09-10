package com.dnd.spaced.core.word.application;

import com.dnd.spaced.core.word.application.dto.request.SearchConditionDto;
import com.dnd.spaced.core.word.application.dto.response.PopularWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadAllWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadWordDto;
import com.dnd.spaced.core.word.application.dto.response.SearchedWordDto;
import com.dnd.spaced.core.word.application.event.dto.WordViewCountIncrementEvent;
import com.dnd.spaced.core.word.application.event.dto.WordViewCountStatisticsEvent;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.Category;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordCondition;
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

    public List<ReadAllWordDto> readAllBy(String categoryName, String lastWordName, Pageable pageable) {
        WordCondition wordCondition = new WordCondition(findCategory(categoryName));
        WordPageRequest wordPageRequest = new WordPageRequest(pageable, lastWordName);

        return wordRepository.findAllBy(wordCondition, wordPageRequest)
                             .stream()
                             .map(ReadAllWordDto::from)
                             .toList();
    }

    public List<SearchedWordDto> search(SearchConditionDto dto) {
        WordSearchCondition wordSearchCondition = new WordSearchCondition(
                dto.name(),
                findCategory(dto.categoryName()),
                dto.pronunciation()
        );
        WordSearchPageRequest wordSearchPageRequest = new WordSearchPageRequest(dto.pageable(), dto.lastWordName());

        return wordRepository.search(wordSearchCondition, wordSearchPageRequest)
                             .stream()
                             .map(SearchedWordDto::from)
                             .toList();
    }

    public ReadWordDto read(Long id) {
        Word word = wordRepository.findBy(id)
                                  .orElseThrow(() -> new WordNotFoundException("지정한 ID에 해당하는 용어를 찾을 수 없습니다."));

        eventPublisher.publishEvent(new WordViewCountIncrementEvent(word.getId(), LocalDateTime.now(clock)));
        eventPublisher.publishEvent(new WordViewCountStatisticsEvent(word.getId(), LocalDateTime.now(clock)));

        return ReadWordDto.from(word);
    }

    public List<PopularWordDto> readPopularWordsAll() {
        return popularWordRepository.findAllBy(LocalDateTime.now(clock))
                                    .stream()
                                    .map(PopularWordDto::from)
                                    .toList();
    }

    private Category findCategory(String categoryName) {
        if (categoryName == null) {
            return null;
        }

        return Category.findBy(categoryName);
    }
}
