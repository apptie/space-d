package com.dnd.spaced.core.quiz.infrastructure.repository;

import com.dnd.spaced.core.quiz.domain.QuizOption;
import org.springframework.data.repository.CrudRepository;

interface QuizOptionCrudRepository extends CrudRepository<QuizOption, Long> {
}
