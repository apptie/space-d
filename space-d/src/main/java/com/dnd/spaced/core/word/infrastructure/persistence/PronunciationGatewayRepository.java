package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QPronunciation.pronunciation;

import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
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
public class PronunciationGatewayRepository implements PronunciationRepository {

    private static final int DEFAULT_BATCH_INSERT_SIZE = 20;

    private final Clock clock;
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;
    private final PronunciationCrudRepository pronunciationCrudRepository;

    @Override
    public void saveAll(List<Pronunciation> pronunciations) {
        String sql = """
                INSERT INTO pronunciations(created_at, updated_at, content, type, word_id)
                VALUES(?, ?, ?, ?, ?);
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Pronunciation pronunciation = pronunciations.get(i);

                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(clock)));
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now(clock)));
                ps.setString(3, pronunciation.getContent());
                ps.setString(4, pronunciation.getType().name());
                ps.setLong(5, pronunciation.getWord().getId());
            }

            @Override
            public int getBatchSize() {
                return Math.min(DEFAULT_BATCH_INSERT_SIZE, pronunciations.size());
            }
        });
    }

    @Override
    public long countBy(Long wordId) {
        return queryFactory.select(pronunciation.id.count())
                           .from(pronunciation)
                           .where(pronunciation.word.id.eq(wordId))
                           .fetchFirst();
    }

    @Override
    public long deleteBy(Long pronunciationId) {
        return queryFactory.delete(pronunciation)
                           .where(pronunciation.id.eq(pronunciationId))
                           .execute();
    }
}
