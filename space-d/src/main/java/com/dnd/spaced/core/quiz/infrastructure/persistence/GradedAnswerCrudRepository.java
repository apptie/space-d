package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import org.springframework.data.repository.CrudRepository;

interface GradedAnswerCrudRepository extends CrudRepository<GradedAnswer, Long> {
}
