package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordPageRequest;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import java.util.List;
import java.util.Optional;

public interface WordRepository {

    Word save(Word word);

    Optional<Word> findBy(Long id);

    List<Word> findAllBy(WordCondition wordCondition, WordPageRequest pageRequest);

    List<Word> findAllBy(WordSearchCondition condition, WordSearchPageRequest pageRequest);
}
