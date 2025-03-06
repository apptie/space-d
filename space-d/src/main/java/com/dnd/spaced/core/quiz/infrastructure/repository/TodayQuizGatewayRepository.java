package com.dnd.spaced.core.quiz.infrastructure.repository;

import static com.dnd.spaced.core.quiz.domain.QTodayQuiz.todayQuiz;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodayQuizGatewayRepository implements TodayQuizRepository {

    private final JPAQueryFactory queryFactory;
    private final TodayQuizCrudRepository todayQuizCrudRepository;

    @Override
    public TodayQuiz save(TodayQuiz todayQuiz) {
        return todayQuizCrudRepository.save(todayQuiz);
    }

    @Override
    public Optional<TodayQuiz> findLatest() {
        TodayQuiz result = queryFactory.selectFrom(todayQuiz)
                                       .leftJoin(todayQuiz.todayQuizQuestion.todayQuizOptions).fetchJoin()
                                       .orderBy(todayQuiz.createdAt.desc())
                                       .limit(1L)
                                       .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<TodayQuiz> findBy(Long todayQuizId) {
        TodayQuiz result = queryFactory.selectFrom(todayQuiz)
                                       .where(todayQuiz.id.eq(todayQuizId))
                                       .leftJoin(todayQuiz.todayQuizQuestion.todayQuizOptions).fetchJoin()
                                       .fetchOne();

        return Optional.ofNullable(result);
    }
}
