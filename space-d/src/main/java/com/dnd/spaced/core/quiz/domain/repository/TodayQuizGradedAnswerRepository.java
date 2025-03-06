package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface TodayQuizGradedAnswerRepository {

    void save(TodayQuizGradedAnswer todayQuizGradedAnswer);

    List<TodayQuizGradedAnswer> findAllBy(Long accountId, Long lastTodayQuizGradedAnswerId, Pageable pageable);

    Optional<TodayQuizGradedAnswer> findBy(Long accountId, Long todayQuizId);
}
