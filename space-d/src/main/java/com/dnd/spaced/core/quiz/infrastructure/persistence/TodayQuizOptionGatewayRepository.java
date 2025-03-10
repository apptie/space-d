package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodayQuizOptionGatewayRepository implements TodayQuizOptionRepository {

    private final TodayQuizOptionCrudRepository todayQuizOptionCrudRepository;

    @Override
    public TodayQuizOption save(TodayQuizOption todayQuizOption) {
        return todayQuizOptionCrudRepository.save(todayQuizOption);
    }
}
