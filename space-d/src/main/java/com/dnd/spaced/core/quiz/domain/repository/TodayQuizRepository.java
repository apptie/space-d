package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizInfo;
import java.util.Optional;

public interface TodayQuizRepository {

    TodayQuiz save(TodayQuiz todayQuiz);

    Optional<SimpleTodayQuizInfo> findLatest();

    Optional<TodayQuiz> findTodayQuizBy(Long todayQuizId);

    Optional<TodayQuiz> findWithTodayQuizOptionBy(Long todayQuizId);
}
