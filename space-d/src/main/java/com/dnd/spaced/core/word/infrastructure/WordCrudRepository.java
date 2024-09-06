package com.dnd.spaced.core.word.infrastructure;

import com.dnd.spaced.core.word.domain.Word;
import org.springframework.data.repository.CrudRepository;

interface WordCrudRepository extends CrudRepository<Word, Long> {
}
