package com.dnd.spaced.core.word.infrastructure.persistence;

import com.dnd.spaced.core.word.domain.WordRandom;
import org.springframework.data.repository.CrudRepository;

public interface WordRandomCrudRepository extends CrudRepository<WordRandom, Long> {
}
