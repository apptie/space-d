package com.dnd.spaced.core.quiz.persistance.repository;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import org.springframework.data.repository.CrudRepository;

public interface TodayQuizCrudRepository extends CrudRepository<TodayQuiz, Long> {
}
