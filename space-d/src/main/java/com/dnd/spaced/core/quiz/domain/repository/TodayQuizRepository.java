package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import java.util.Optional;

public interface TodayQuizRepository {

    TodayQuiz save(TodayQuiz todayQuiz);

    Optional<TodayQuiz> findLatest();

    Optional<TodayQuiz> findBy(Long todayQuizId);
}
