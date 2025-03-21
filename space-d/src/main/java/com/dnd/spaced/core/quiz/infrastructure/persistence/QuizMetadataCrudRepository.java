package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.QuizMetadata;
import org.springframework.data.repository.CrudRepository;

public interface QuizMetadataCrudRepository extends CrudRepository<QuizMetadata, Long> {
}
