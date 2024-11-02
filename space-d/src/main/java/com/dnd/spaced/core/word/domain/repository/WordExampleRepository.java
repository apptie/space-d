package com.dnd.spaced.core.word.domain.repository;

public interface WordExampleRepository {

    long countBy(Long wordId);

    long update(Long id, String example);

    void deleteBy(Long id);
}
