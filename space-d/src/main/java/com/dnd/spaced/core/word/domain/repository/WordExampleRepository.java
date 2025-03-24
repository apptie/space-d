package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.WordExample;
import java.util.List;

public interface WordExampleRepository {

    void saveAll(List<WordExample> wordExamples);

    long countBy(Long wordId);

    long update(Long wordExampleId, String example);

    void deleteBy(Long wordExampleId);
}
