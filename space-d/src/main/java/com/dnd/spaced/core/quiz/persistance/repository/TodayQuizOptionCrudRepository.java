package com.dnd.spaced.core.quiz.persistance.repository;

import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import org.springframework.data.repository.CrudRepository;

interface TodayQuizOptionCrudRepository extends CrudRepository<TodayQuizOption, Long> {
}
