package com.dnd.spaced.core.quiz.infrastructure.persistence;

import static com.dnd.spaced.core.quiz.domain.QTodayQuiz.todayQuiz;

import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.dnd.spaced.global.consts.CacheConst;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

    private static final RowMapper<SimpleTodayQuizDto> simpleTodayQuizDtoRowMapper = new SimpleTodayQuizDtoMapper();

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
    public Optional<SimpleTodayQuizDto> findLatest() {
        String sql = """
                SELECT
                    tq.id,
                    tq.created_at,
                    tq.question,
                    tq.passage,
                    tq.quiz_category,
                    tq.answer_content,
                    tq.answer_word_id
                FROM (
                    SELECT id
                    FROM today_quizzes
                    ORDER BY id DESC
                    LIMIT 1
                ) t left join today_quizzes tq ON t.id = tq.id;
                """;
        try {
            SimpleTodayQuizDto simpleTodayQuizDto = jdbcTemplate.queryForObject(sql, simpleTodayQuizDtoRowMapper);

            return Optional.of(simpleTodayQuizDto);
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

    private static class SimpleTodayQuizDtoMapper implements RowMapper<SimpleTodayQuizDto> {

        @Override
        public SimpleTodayQuizDto mapRow(ResultSet rs, int ignoreRowNum) throws SQLException {
            Long id = rs.getLong(1);
            LocalDateTime createdAt = rs.getTimestamp(2)
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime();
            String question = rs.getString(3);
            String questionContent = rs.getString(4);
            QuizCategory quizCategory = QuizCategory.valueOf(rs.getString(5));
            String answerContent = rs.getString(6);
            Long answerWordId = rs.getLong(7);
            TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(answerWordId, answerContent);

            return new SimpleTodayQuizDto(
                    id,
                    quizCategory,
                    question,
                    questionContent,
                    todayQuizAnswerOption,
                    createdAt
            );
        }
    }
}
