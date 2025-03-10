package com.dnd.spaced.core.word.domain.repository;

public interface WordExampleRepository {

    long countBy(Long wordExampleId);

    long update(Long wordExampleId, String example);

    void deleteBy(Long wordExampleId);
}
