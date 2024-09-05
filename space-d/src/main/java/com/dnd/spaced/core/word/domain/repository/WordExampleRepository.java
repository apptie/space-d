package com.dnd.spaced.core.word.domain.repository;

public interface WordExampleRepository {

    void update(Long id, String example);

    void deleteBy(Long id);
}
