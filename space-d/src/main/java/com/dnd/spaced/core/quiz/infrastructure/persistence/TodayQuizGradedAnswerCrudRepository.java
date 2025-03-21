package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import org.springframework.data.repository.CrudRepository;

public interface TodayQuizGradedAnswerCrudRepository extends CrudRepository<TodayQuizGradedAnswer, Long> {
}
