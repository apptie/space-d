package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import org.springframework.data.repository.CrudRepository;

public interface TodayQuizCrudRepository extends CrudRepository<TodayQuiz, Long> {
}
