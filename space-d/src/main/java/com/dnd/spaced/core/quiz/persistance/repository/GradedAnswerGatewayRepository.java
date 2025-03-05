package com.dnd.spaced.core.quiz.persistance.repository;

import static com.dnd.spaced.core.quiz.domain.QGradedAnswer.gradedAnswer;

import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.GradedAnswerRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GradedAnswerGatewayRepository implements GradedAnswerRepository {

    private final JPAQueryFactory queryFactory;
    private final GradedAnswerCrudRepository gradedAnswerCrudRepository;

    @Override
    public void saveAll(List<GradedAnswer> gradedAnswers) {
        gradedAnswerCrudRepository.saveAll(gradedAnswers);
    }

    @Override
    public List<GradedAnswer> findAllBy(Long accountId, Long lastGradedAnswerId, Pageable pageable) {
        return queryFactory.selectFrom(gradedAnswer)
                           .where(gradedAnswer.accountId.eq(accountId), ltLastGradedAnswerId(lastGradedAnswerId))
                           .leftJoin(gradedAnswer.quizQuestion).fetchJoin()
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    @Override
    public List<GradedAnswer> findAllBy(Long quizId) {
        return queryFactory.selectFrom(gradedAnswer)
                           .where(gradedAnswer.quizId.eq(quizId))
                           .leftJoin(gradedAnswer.quizQuestion).fetchJoin()
                           .fetch();
    }

    private BooleanExpression ltLastGradedAnswerId(Long lastGradedAnswerId) {
        if (lastGradedAnswerId == null) {
            return null;
        }

        return gradedAnswer.id.lt(lastGradedAnswerId);
    }
}
