package com.dnd.spaced.core.word.infrastructure;

import com.dnd.spaced.core.word.domain.WordExample;
import org.springframework.data.repository.CrudRepository;

interface WordExampleCrudRepository extends CrudRepository<WordExample, Long> {
}
