package com.dnd.spaced.core.word.domain.repository;

public interface PronunciationRepository {

    long countBy(Long pronunciationId);

    void deleteBy(Long pronunciationId);
}
