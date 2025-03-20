package com.dnd.spaced.core.quiz.infrastructure.persistence;

import static com.dnd.spaced.core.quiz.domain.QTodayQuizGradedAnswer.todayQuizGradedAnswer;

import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizGradedAnswerRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodayQuizGradedAnswerGatewayRepository implements TodayQuizGradedAnswerRepository {

    private final JPAQueryFactory queryFactory;
    private final TodayQuizGradedAnswerCrudRepository todayQuizGradedAnswerCrudRepository;

    @Override
    public void save(TodayQuizGradedAnswer todayQuizGradedAnswer) {
        todayQuizGradedAnswerCrudRepository.save(todayQuizGradedAnswer);
    }

    @Override
    public List<TodayQuizGradedAnswer> findAllBy(Long accountId, Long lastTodayQuizGradedAnswerId, Pageable pageable) {
        return queryFactory.selectFrom(todayQuizGradedAnswer)
                           .where(
                                   todayQuizGradedAnswer.accountId.eq(accountId),
                                   ltLastTodayQuizGradedAnswerId(lastTodayQuizGradedAnswerId)
                           )
                           .leftJoin(todayQuizGradedAnswer.todayQuiz).fetchJoin()
                           .limit(pageable.getPageSize())
                           .orderBy(todayQuizGradedAnswer.id.desc())
                           .fetch();
    }

    @Override
    public Optional<TodayQuizGradedAnswer> findBy(Long accountId, Long todayQuizId) {
        TodayQuizGradedAnswer result = queryFactory.selectFrom(todayQuizGradedAnswer)
                                                   .where(
                                                           todayQuizGradedAnswer.todayQuiz.id.eq(todayQuizId),
                                                           todayQuizGradedAnswer.accountId.eq(accountId)
                                                   )
                                                   .leftJoin(todayQuizGradedAnswer.todayQuiz).fetchJoin()
                                                   .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsBy(Long accountId, Long todayQuizId) {
        Long result = queryFactory.select(todayQuizGradedAnswer.id)
                                  .from(todayQuizGradedAnswer)
                                  .where(
                                          todayQuizGradedAnswer.accountId.eq(accountId),
                                          todayQuizGradedAnswer.todayQuiz.id.eq(todayQuizId)
                                  )
                                  .fetchOne();

        return result != null;
    }

    private BooleanExpression ltLastTodayQuizGradedAnswerId(Long lastTodayQuizGradedAnswerId) {
        if (lastTodayQuizGradedAnswerId == null) {
            return null;
        }

        return todayQuizGradedAnswer.id.lt(lastTodayQuizGradedAnswerId);
    }
}
