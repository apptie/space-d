package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.Pronunciation;
import java.util.List;
import java.util.Optional;

public interface PronunciationRepository {

    Optional<Pronunciation> findBy(Long pronunciationId);

    void saveAll(List<Pronunciation> pronunciations);

    long countBy(Long wordId);

    long deleteBy(Long pronunciationId);
}
