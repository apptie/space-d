package com.dnd.spaced.core.word.infrastructure;

import com.dnd.spaced.core.word.domain.Pronunciation;
import org.springframework.data.repository.CrudRepository;

public interface PronunciationCrudRepository extends CrudRepository<Pronunciation, Long> {
}
