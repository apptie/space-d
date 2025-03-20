package com.dnd.spaced.core.quiz.infrastructure.persistence;

import static com.dnd.spaced.core.quiz.domain.QTodayQuiz.todayQuiz;
import static com.dnd.spaced.core.quiz.domain.QTodayQuizOption.todayQuizOption;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.dto.TodayQuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.mapper.TodayQuizInfoMapper;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
                                       .orderBy(todayQuiz.id.desc())
                                       .limit(1L)
                                       .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<TodayQuiz> findTodayQuizBy(Long todayQuizId) {
        TodayQuiz result = queryFactory.selectFrom(todayQuiz)
                                       .where(todayQuiz.id.eq(todayQuizId))
                                       .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<TodayQuizInfo> findTodayQuizInfoBy(Long todayQuizId) {
        List<Tuple> results = queryFactory.select(todayQuiz, todayQuizOption)
                                          .from(todayQuiz)
                                          .leftJoin(todayQuizOption)
                                          .on(todayQuiz.id.eq(todayQuizOption.todayQuizId))
                                          .where(todayQuiz.id.eq(todayQuizId))
                                          .fetch();

        if (results.isEmpty()) {
            return Optional.empty();
        }

        TodayQuiz quiz = results.get(0)
                                .get(todayQuiz);
        List<TodayQuizOption> quizOptions = results.stream()
                                                   .map(tuple -> tuple.get(todayQuizOption))
                                                   .filter(Objects::nonNull)
                                                   .sorted(Comparator.comparingInt(TodayQuizOption::getOptionOrder))
                                                   .toList();
        TodayQuizInfo todayQuizInfo = TodayQuizInfoMapper.toDto(quiz, quizOptions);

        return Optional.of(todayQuizInfo);
    }
}
