package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface WordViewRepository {

    Optional<WordView> findBy(Long wordId);

    List<WordView> findAllBy(Category category, String lastWordName, Category lastCategory, Pageable pageable);

    List<WordView> search(WordSearchCondition condition, WordSearchPageRequest pageRequest);
}
