package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordPageRequest;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import java.util.List;
import java.util.Optional;

public interface WordRepository {

    Word save(Word word);

    void updateViewCount(Long id);

    void updateViewCount(List<WordViewCountStatisticsDto> wordViewCountStatisticsDtos);

    Optional<Word> findBy(Long id);

    List<String> findNameAllBy(Long[] ids);

    List<Word> findAllBy(WordCondition wordCondition, WordPageRequest pageRequest);

    List<Word> search(WordSearchCondition condition, WordSearchPageRequest pageRequest);
}
