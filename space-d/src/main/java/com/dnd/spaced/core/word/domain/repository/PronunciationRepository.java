package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.Pronunciation;
import java.util.List;

public interface PronunciationRepository {

    void saveAll(List<Pronunciation> pronunciations);

    long countBy(Long wordId);

    void deleteBy(Long pronunciationId);
}
