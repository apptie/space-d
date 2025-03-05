package com.dnd.spaced.core.quiz.persistance.repository;

import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

interface TodayQuizGradedAnswerCrudRepository extends CrudRepository<TodayQuizGradedAnswer, Long> {

    List<TodayQuizGradedAnswer> findAllByAccountId(Long accountId);
}
