package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.dto.TodayQuizInfo;
import java.util.Optional;

public interface TodayQuizRepository {

    TodayQuiz save(TodayQuiz todayQuiz);

    Optional<TodayQuiz> findLatest();

    Optional<TodayQuiz> findTodayQuizBy(Long todayQuizId);

    Optional<TodayQuizInfo> findTodayQuizInfoBy(Long todayQuizId);
}
