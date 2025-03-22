package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import java.util.List;
import java.util.Optional;

public interface WordRepository {

    Word save(Word word);

    boolean existsBy(Long wordId);

    void updateViewCount(Long wordId);

    void updateViewCount(List<WordViewCountStatisticsDto> wordViewCountStatisticsDtos);

    void addBookmarkCount(Long wordId);

    void updateSubtractBookmarkCount(Long wordId);

    List<String> findNameAllBy(Long[] wordIds);

    Optional<Word> findBy(Long wordId);
}
