package com.dnd.spaced.core.word.domain.repository;

public interface WordExampleRepository {

    long countBy(Long wordId);

    long update(Long wordExampleId, String example);

    void deleteBy(Long wordExampleId);
}
