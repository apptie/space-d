package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import org.springframework.data.repository.CrudRepository;

interface QuizQuestionCrudRepository extends CrudRepository<QuizQuestion, Long> {
}
