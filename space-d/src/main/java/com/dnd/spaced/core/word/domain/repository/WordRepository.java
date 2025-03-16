package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.dto.WordInfo;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
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

    Optional<WordInfo> findBy(Long wordId);

    List<String> findNameAllBy(Long[] wordIds);

    List<WordInfo> findAllBy(Category category, String lastWordName, Category lastCategory, Pageable pageable);

    List<Word> findRandomAllBy(List<Long> wordIds);

    List<WordInfo> search(WordSearchCondition condition, WordSearchPageRequest pageRequest);
}
