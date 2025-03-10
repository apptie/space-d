package com.dnd.spaced.core.word.infrastructure.persistence;

import com.dnd.spaced.core.word.domain.WordRandom;
import org.springframework.data.repository.CrudRepository;

interface WordRandomCrudRepository extends CrudRepository<WordRandom, Long> {
}
