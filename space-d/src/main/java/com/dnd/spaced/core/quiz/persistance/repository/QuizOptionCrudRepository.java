package com.dnd.spaced.core.quiz.persistance.repository;

import com.dnd.spaced.core.quiz.domain.QuizOption;
import org.springframework.data.repository.CrudRepository;

interface QuizOptionCrudRepository extends CrudRepository<QuizOption, Long> {
}
