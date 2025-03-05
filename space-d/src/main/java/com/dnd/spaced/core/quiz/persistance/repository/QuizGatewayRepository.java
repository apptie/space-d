package com.dnd.spaced.core.quiz.persistance.repository;

import static com.dnd.spaced.core.quiz.domain.QQuiz.quiz;

import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.repository.QuizRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizGatewayRepository implements QuizRepository {

    private final JPAQueryFactory queryFactory;
    private final QuizCrudRepository quizCrudRepository;

    @Override
    public Quiz save(Quiz quiz) {
        return quizCrudRepository.save(quiz);
    }

    @Override
    public List<Quiz> findAllBy(Long accountId) {
        return queryFactory.selectFrom(quiz)
                           .where(quiz.accountId.eq(accountId))
                           .leftJoin(quiz.quizQuestions).fetchJoin()
                           .fetch();
    }

    @Override
    public Optional<Quiz> findBy(Long id) {
        Quiz result = queryFactory.selectFrom(quiz)
                                  .where(quiz.id.eq(id))
                                  .leftJoin(quiz.quizQuestions).fetchJoin()
                                  .fetchOne();

        return Optional.ofNullable(result);
    }
}
