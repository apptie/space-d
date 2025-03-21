package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import java.util.List;

public interface TodayQuizOptionRepository {

    TodayQuizOption save(TodayQuizOption todayQuizOption);

    void saveAll(List<TodayQuizOption> todayQuizOptions);
}
