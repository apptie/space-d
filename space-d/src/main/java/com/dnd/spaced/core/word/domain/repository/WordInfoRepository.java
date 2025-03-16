package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.dto.WordInfo;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface WordInfoRepository {

    Optional<WordInfo> findBy(Long wordId);

    List<WordInfo> findAllBy(Category category, String lastWordName, Category lastCategory, Pageable pageable);

    List<WordInfo> search(WordSearchCondition condition, WordSearchPageRequest pageRequest);
}
