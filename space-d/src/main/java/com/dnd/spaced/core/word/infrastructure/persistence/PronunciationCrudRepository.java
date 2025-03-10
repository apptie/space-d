package com.dnd.spaced.core.word.infrastructure.persistence;

import com.dnd.spaced.core.word.domain.Pronunciation;
import org.springframework.data.repository.CrudRepository;

interface PronunciationCrudRepository extends CrudRepository<Pronunciation, Long> {
}
