package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QWordExample.wordExample;

import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class WordExampleGatewayRepository implements WordExampleRepository {

    private final Clock clock;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<WordExample> findBy(Long wordExampleId) {
        WordExample result = queryFactory.selectFrom(wordExample)
                                         .where(wordExample.id.eq(wordExampleId))
                                         .fetchOne();

        return Optional.ofNullable(result);
    }

    public WordExampleGatewayRepository(Clock clock, JdbcTemplate jdbcTemplate, JPAQueryFactory queryFactory) {
        this.clock = clock;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.queryFactory = queryFactory;
    }

    public long countBy(Long wordId) {
        String sql = """
                SELECT COUNT(id) FROM word_examples WHERE word_id = :wordId
                """;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("wordId", wordId);

        Long result = namedParameterJdbcTemplate.queryForObject(sql, parameters, Long.class);
        return result != null ? result : 0L;
    }

    @Override
    public long update(Long wordExampleId, String example) {
        return queryFactory.update(wordExample)
                           .set(wordExample.content, example)
                           .where(wordExample.id.eq(wordExampleId))
                           .execute();
    }

    @Override
    public long deleteBy(Long wordExampleId) {
        return queryFactory.delete(wordExample)
                           .where(wordExample.id.eq(wordExampleId))
                           .execute();
    }

    @Override
    public void saveAll(List<WordExample> wordExamples) {
        String sql = """
                INSERT INTO word_examples(created_at, updated_at, content, word_id, deleted)
                VALUES(:createdAt, :updatedAt, :example, :wordId, false)
                """;
        List<SqlParameterSource> parameterSources = new ArrayList<>();

        for (WordExample wordExample : wordExamples) {
            parameterSources.add(
                    new MapSqlParameterSource()
                            .addValue("createdAt", Timestamp.valueOf(LocalDateTime.now(clock)))
                            .addValue("updatedAt", Timestamp.valueOf(LocalDateTime.now(clock)))
                            .addValue("example", wordExample.getContent())
                            .addValue("wordId", wordExample.getWord().getId())
            );
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources.toArray(SqlParameterSource[]::new));
    }
}
