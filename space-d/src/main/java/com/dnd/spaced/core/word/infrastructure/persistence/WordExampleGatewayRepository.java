package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QWordExample.wordExample;

import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordExampleGatewayRepository implements WordExampleRepository {

    private static final int DEFAULT_BATCH_INSERT_SIZE = 10;

    private final Clock clock;
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    public long countBy(Long wordId) {
        return queryFactory.select(wordExample.id.count())
                           .from(wordExample)
                           .where(wordExample.word.id.eq(wordId))
                           .fetchFirst();
    }

    @Override
    public long update(Long wordExampleId, String example) {
        return queryFactory.update(wordExample)
                           .set(wordExample.example, example)
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
                INSERT INTO word_examples(created_at, updated_at, example, word_id)
                VALUES(?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                WordExample wordExample = wordExamples.get(i);

                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(clock)));
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now(clock)));
                ps.setString(3, wordExample.getExample());
                ps.setLong(4, wordExample.getWord().getId());
            }

            @Override
            public int getBatchSize() {
                return Math.min(DEFAULT_BATCH_INSERT_SIZE, wordExamples.size());
            }
        });
    }
}
