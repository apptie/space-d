package com.dnd.spaced.core.word.domain.repository;

public interface PronunciationRepository {

    long countBy(Long wordId);

    void deleteBy(Long id);
}
