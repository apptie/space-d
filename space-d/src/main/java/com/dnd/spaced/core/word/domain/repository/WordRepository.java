package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.Category;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface WordRepository {

    Word save(Word word);

    boolean existsBy(Long wordId);

    void updateViewCount(Long wordId);

    void updateViewCount(List<WordViewCountStatisticsDto> wordViewCountStatisticsDtos);

    void addBookmarkCount(Long wordId);

    void updateSubtractBookmarkCount(Long wordId);

    Optional<Word> findBy(Long wordId);

    List<String> findNameAllBy(Long[] wordIds);

    List<Word> findAllBy(Category category, String lastWordName, Pageable pageable);

    List<Word> findAllBy(List<Long> wordIds);

    List<Word> search(WordSearchCondition condition, WordSearchPageRequest pageRequest);
}
