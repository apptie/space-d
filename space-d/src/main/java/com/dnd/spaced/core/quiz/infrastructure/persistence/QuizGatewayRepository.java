package com.dnd.spaced.core.quiz.infrastructure.persistence;

import static com.dnd.spaced.core.quiz.domain.QQuiz.quiz;
import static com.dnd.spaced.core.quiz.domain.QQuizOption.quizOption;

import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.mapper.QuizInfoMapper;
import com.dnd.spaced.core.quiz.domain.repository.QuizRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuizGatewayRepository implements QuizRepository {

    private static final RowMapper<QuizInfo> quizInfoRowMapper = (rs, ignoreRowNum) -> new QuizInfo(
            rs.getLong(1),
            rs.getLong(2),
            rs.getBoolean(3),
            rs.getTimestamp(4).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            Collections.emptyList()
    );

    private final JPAQueryFactory queryFactory;
    private final QuizCrudRepository quizCrudRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public QuizGatewayRepository(
            JPAQueryFactory queryFactory,
            QuizCrudRepository quizCrudRepository,
            JdbcTemplate jdbcTemplate
    ) {
        this.queryFactory = queryFactory;
        this.quizCrudRepository = quizCrudRepository;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

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
    public Optional<Quiz> findBy(Long quizId) {
        Quiz result = queryFactory.selectFrom(quiz)
                                  .where(quiz.id.eq(quizId))
                                  .leftJoin(quiz.quizQuestions).fetchJoin()
                                  .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<QuizInfo> findBy(Long quizId, Long accountId) {
        Quiz result = queryFactory.selectFrom(quiz)
                                  .where(quiz.id.eq(quizId), quiz.accountId.eq(accountId))
                                  .leftJoin(quiz.quizQuestions).fetchJoin()
                                  .fetchOne();

        if (result == null) {
            return Optional.empty();
        }

        List<QuizQuestion> quizQuestions = result.getQuizQuestions();
        List<Long> quizQuestionId = quizQuestions.stream()
                                                 .map(QuizQuestion::getId)
                                                 .toList();
        Map<Long, List<QuizOption>> quizOptionMap = queryFactory.selectFrom(quizOption)
                                                                .where(quizOption.quizQuestionId.in(quizQuestionId))
                                                                .fetch()
                                                                .stream()
                                                                .collect(Collectors.groupingBy(
                                                                        QuizOption::getQuizQuestionId,
                                                                        Collectors.mapping(
                                                                                Function.identity(),
                                                                                Collectors.toList()
                                                                        )
                                                                ));
        QuizInfo quizInfo = QuizInfoMapper.toDto(result, quizOptionMap);

        return Optional.of(quizInfo);
    }

    @Override
    public List<QuizInfo> findAllBy(Long accountId, Long lastQuizId, Pageable pageable) {
        String sql = """
        SELECT q.id, q.account_id, q.solved, q.created_at
        FROM (
            SELECT id
            FROM quizzes
            WHERE account_id = :accountId
        """;

        if (lastQuizId != null) {
            sql = sql.concat(" AND id < :lastQuizId");
        }

        sql = sql.concat(" ORDER BY id DESC LIMIT :limit) t left join quizzes q on t.id = q.id");

        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
                .addValue("accountId", accountId)
                .addValue("limit", pageable.getPageSize());

        if (lastQuizId != null) {
            sqlParameters.addValue("lastQuizId", lastQuizId);
        }

        return namedParameterJdbcTemplate.query(sql, sqlParameters, quizInfoRowMapper);
    }
}
