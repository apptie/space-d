package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QPronunciation.pronunciation;

import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
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
public class PronunciationGatewayRepository implements PronunciationRepository {

    private final Clock clock;
    private final JPAQueryFactory queryFactory;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PronunciationCrudRepository pronunciationCrudRepository;

    public PronunciationGatewayRepository(
            Clock clock,
            JdbcTemplate jdbcTemplate,
            JPAQueryFactory queryFactory,
            PronunciationCrudRepository pronunciationCrudRepository
    ) {
        this.clock = clock;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.queryFactory = queryFactory;
        this.pronunciationCrudRepository = pronunciationCrudRepository;
    }

    @Override
    public Optional<Pronunciation> findBy(Long pronunciationId) {
        return pronunciationCrudRepository.findById(pronunciationId);
    }

    @Override
    public void saveAll(List<Pronunciation> pronunciations) {
        String sql = """
                INSERT INTO pronunciations(created_at, updated_at, content, pronunciation_type, word_id, deleted)
                VALUES(:createdAt, :updatedAt, :content, :type, :wordId, false);
                """;
        List<SqlParameterSource> parameterSources = new ArrayList<>();

        for (Pronunciation pronunciation : pronunciations) {
            parameterSources.add(
                    new MapSqlParameterSource()
                            .addValue("createdAt", Timestamp.valueOf(LocalDateTime.now(clock)))
                            .addValue("updatedAt", Timestamp.valueOf(LocalDateTime.now(clock)))
                            .addValue("content", pronunciation.getContent())
                            .addValue("type", pronunciation.getPronunciationType().name())
                            .addValue("wordId", pronunciation.getWord().getId())
            );
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources.toArray(SqlParameterSource[]::new));
    }

    @Override
    public long countBy(Long wordId) {
        String sql = """
                SELECT COUNT(id) FROM pronunciations WHERE word_id = :wordId
                """;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("wordId", wordId);

        Long result = namedParameterJdbcTemplate.queryForObject(sql, parameters, Long.class);
        return result != null ? result : 0L;
    }
}
