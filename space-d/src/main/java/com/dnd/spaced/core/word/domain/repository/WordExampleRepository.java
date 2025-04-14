package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.WordExample;
import java.util.List;
import java.util.Optional;

public interface WordExampleRepository {

    Optional<WordExample> findBy(Long wordExampleId);

    void saveAll(List<WordExample> wordExamples);

    long countBy(Long wordId);

    long update(Long wordExampleId, String example);

    void deleteAllBy(Long wordId);
}
