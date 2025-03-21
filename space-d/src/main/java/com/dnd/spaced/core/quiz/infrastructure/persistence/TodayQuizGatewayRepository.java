package com.dnd.spaced.core.quiz.infrastructure.persistence;

import static com.dnd.spaced.core.quiz.domain.QTodayQuiz.todayQuiz;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.mapper.TodayQuizInfoMapper;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.dnd.spaced.global.consts.CacheConst;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodayQuizGatewayRepository implements TodayQuizRepository {

    private static final RowMapper<SimpleTodayQuizInfo> simpleTodayQuizInfoRowMapper =
            (rs, ignoreRowNum) -> TodayQuizInfoMapper.toDto(
                    rs.getLong(1),
                    rs.getTimestamp(2).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    rs.getString(3),
                    rs.getString(4),
                    QuizCategory.valueOf(rs.getString(5)),
                    rs.getString(6),
                    rs.getLong(7)
            );

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;
    private final TodayQuizCrudRepository todayQuizCrudRepository;

    @Override
    public TodayQuiz save(TodayQuiz todayQuiz) {
        return todayQuizCrudRepository.save(todayQuiz);
    }

    @Override
    @Cacheable(
            value = CacheConst.TODAY_QUIZ_CACHE_NAME,
            key = "'" + CacheConst.TODAY_QUIZ_CACHE_NAME + "'",
            cacheManager = "memoryCacheManager"
    )
    public Optional<SimpleTodayQuizInfo> findLatest() {
        String sql = """
                SELECT
                    tq.id,
                    tq.created_at,
                    tq.question,
                    tq.question_content,
                    tq.quiz_category,
                    tq.content,
                    tq.word_id
                FROM (
                    SELECT id
                    FROM today_quizzes
                    ORDER BY id DESC
                    LIMIT 1
                ) t left join today_quizzes tq ON t.id = tq.id;
                """;
        try {
            SimpleTodayQuizInfo simpleTodayQuizInfo = jdbcTemplate.queryForObject(sql, simpleTodayQuizInfoRowMapper);

            return Optional.of(simpleTodayQuizInfo);
        } catch (IncorrectResultSizeDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TodayQuiz> findTodayQuizBy(Long todayQuizId) {
        TodayQuiz result = queryFactory.selectFrom(todayQuiz)
                                       .where(todayQuiz.id.eq(todayQuizId))
                                       .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<TodayQuiz> findWithTodayQuizOptionBy(Long todayQuizId) {
        TodayQuiz result = queryFactory.selectFrom(todayQuiz)
                                       .where(todayQuiz.id.eq(todayQuizId))
                                       .leftJoin(todayQuiz.todayQuizQuestion.todayQuizOptions).fetchJoin()
                                       .fetchOne();

        return Optional.ofNullable(result);
    }
}
