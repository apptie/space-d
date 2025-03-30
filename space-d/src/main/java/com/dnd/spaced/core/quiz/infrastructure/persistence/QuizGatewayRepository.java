package com.dnd.spaced.core.quiz.infrastructure.persistence;

import static com.dnd.spaced.core.quiz.domain.QQuiz.quiz;
import static com.dnd.spaced.core.quiz.domain.QQuizOption.quizOption;

import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.dto.QuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.SimpleQuizInfo;
import com.dnd.spaced.core.quiz.domain.dto.SimpleQuizInfo.QuizQuestionInfo;
import com.dnd.spaced.core.quiz.domain.dto.mapper.QuizInfoMapper;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.QuizRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    private static final RowMapper<SimpleQuizValue> simpleQuizRowMapper = (rs, ignoreRowNum) -> new SimpleQuizValue(
            rs.getLong(1),
            rs.getLong(2),
            rs.getBoolean(3),
            rs.getTimestamp(4).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            QuizCategory.valueOf(rs.getString(5)),
            rs.getString(6)
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
        Quiz result = findQuizFetchJoinWithQuizQuestions(quizId, accountId);

        if (result == null) {
            return Optional.empty();
        }

        List<Long> quizQuestionId = findQuizQuestionIds(result);
        Map<Long, List<QuizOption>> quizOptionMap = findQuizOptions(quizQuestionId);
        QuizInfo quizInfo = QuizInfoMapper.toDto(result, quizOptionMap);

        return Optional.of(quizInfo);
    }

    @Override
    public List<SimpleQuizInfo> findAllBy(Long accountId, Long lastQuizId, Pageable pageable) {
        String sql = calculateFinAllSql(lastQuizId);
        MapSqlParameterSource sqlParameters = calculateSqlParameters(accountId, lastQuizId, pageable);

        return namedParameterJdbcTemplate.query(sql, sqlParameters, simpleQuizRowMapper)
                                         .stream()
                                         .collect(Collectors.groupingBy(
                                                 simpleQuizValue -> new SimpleQuizKey(
                                                         simpleQuizValue.id,
                                                         simpleQuizValue.accountId,
                                                         simpleQuizValue.solved,
                                                         simpleQuizValue.createdAt
                                                 ),
                                                 Collectors.mapping(
                                                         simpleQuizValue -> new QuizQuestionInfo(
                                                                 simpleQuizValue.quizCategory,
                                                                 simpleQuizValue.questionContent
                                                         ),
                                                         Collectors.toList()
                                                 )
                                         ))
                                         .entrySet()
                                         .stream()
                                         .map(entry -> new SimpleQuizInfo(
                                                 entry.getKey().id,
                                                 entry.getKey().accountId,
                                                 entry.getKey().solved,
                                                 entry.getKey().createdAt,
                                                 entry.getValue()
                                         ))
                                         .toList();
    }

    private Quiz findQuizFetchJoinWithQuizQuestions(Long quizId, Long accountId) {
        return queryFactory.selectFrom(quiz)
                           .where(quiz.id.eq(quizId), quiz.accountId.eq(accountId))
                           .leftJoin(quiz.quizQuestions).fetchJoin()
                           .fetchOne();
    }

    private List<Long> findQuizQuestionIds(Quiz result) {
        List<QuizQuestion> quizQuestions = result.getQuizQuestions();

        return quizQuestions.stream()
                            .map(QuizQuestion::getId)
                            .toList();
    }

    private Map<Long, List<QuizOption>> findQuizOptions(List<Long> quizQuestionId) {
        return queryFactory.selectFrom(quizOption)
                           .where(quizOption.quizQuestionId.in(quizQuestionId))
                           .fetch()
                           .stream()
                           .collect(Collectors.groupingBy(
                                   QuizOption::getQuizQuestionId,
                                   Collectors.mapping(Function.identity(), Collectors.toList())
                           ));
    }

    private String calculateFinAllSql(Long lastQuizId) {
        String sql = """
                SELECT q.id, q.account_id, q.solved, q.created_at, qq.quiz_category, qq.passage
                FROM (
                    SELECT id
                    FROM quizzes
                    WHERE account_id = :accountId
                """;

        if (lastQuizId != null) {
            sql = sql.concat(" AND id < :lastQuizId");
        }

        sql = sql.concat(" ORDER BY id DESC LIMIT :limit) t LEFT JOIN quizzes q ON t.id = q.id");
        sql = sql.concat(" LEFT JOIN quiz_questions qq ON qq.quiz_id = q.id");
        return sql;
    }

    private MapSqlParameterSource calculateSqlParameters(Long accountId, Long lastQuizId, Pageable pageable) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
                .addValue("accountId", accountId)
                .addValue("limit", pageable.getPageSize());

        if (lastQuizId != null) {
            sqlParameters.addValue("lastQuizId", lastQuizId);
        }
        return sqlParameters;
    }

    private record SimpleQuizKey(Long id, Long accountId, boolean solved, LocalDateTime createdAt) {
    }

    private record SimpleQuizValue(
            Long id,
            Long accountId,
            boolean solved,
            LocalDateTime createdAt,
            QuizCategory quizCategory,
            String questionContent
    ) {
    }
}
