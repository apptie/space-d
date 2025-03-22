package com.dnd.spaced.core.word.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordRandom;
import com.dnd.spaced.core.word.domain.dto.SimpleWordInfo;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WordRandomGatewayRepository implements WordRandomRepository {

    private static final int RANDOM_BOUND = 1_000_000;
    private static final RowMapper<SimpleWordInfo> simpleWordInfoRowMapper = (rs, ignoreRowNum) -> new SimpleWordInfo(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4)
    );

    private final WordRandomCrudRepository wordRandomCrudRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public WordRandomGatewayRepository(
            WordRandomCrudRepository wordRandomCrudRepository,
            JdbcTemplate jdbcTemplate
    ) {
        this.wordRandomCrudRepository = wordRandomCrudRepository;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void saveWith(Word word, Category category) {
        int random = ThreadLocalRandom.current().nextInt(RANDOM_BOUND);
        WordRandom wordRandom = new WordRandom(word, category, random);

        wordRandomCrudRepository.save(wordRandom);
    }

    @Override
    public List<SimpleWordInfo> findRandomAllBy(QuizCategory quizCategory, long limit) {
        int random = ThreadLocalRandom.current().nextInt(RANDOM_BOUND);

        List<SimpleWordInfo> result = findGoe(random, quizCategory, limit);

        if (result.size() < limit) {
            result.addAll(findLoe(random, quizCategory, limit));
        }

        return result.subList(0, (int) (limit));
    }

    private List<SimpleWordInfo> findGoe(int random, QuizCategory quizCategory, long limit) {
        String sql = """
                SELECT
                    w.id,
                    w.category,
                    w.name,
                    w.meaning
                FROM (
                    SELECT
                        word_id
                    FROM
                        word_randoms
                    WHERE
                        random >= :random
                """;

        if (quizCategory.isNotTotal()) {
            sql = sql.concat(" AND category = :category");
        }

        sql = sql.concat(" LIMIT :limit ) wr LEFT JOIN words w ON w.id = wr.word_id");

        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
                .addValue("random", random)
                .addValue("limit", limit);

        if (quizCategory.isNotTotal()) {
            sqlParameters.addValue("category", quizCategory.name());
        }

        return namedParameterJdbcTemplate.query(sql, sqlParameters, simpleWordInfoRowMapper);
    }

    private List<SimpleWordInfo> findLoe(int random, QuizCategory quizCategory, long limit) {
        String sql = """
                SELECT
                    w.id,
                    w.category,
                    w.name,
                    w.meaning
                FROM (
                    SELECT
                        word_id
                    FROM
                        word_randoms
                    WHERE
                        random <= :random
                """;

        if (quizCategory.isNotTotal()) {
            sql = sql.concat(" AND category = :category");
        }

        sql = sql.concat(" LIMIT :limit ) wr LEFT JOIN words w ON w.id = wr.word_id");

        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
                .addValue("random", random)
                .addValue("limit", limit);

        if (quizCategory.isNotTotal()) {
            sqlParameters.addValue("category", quizCategory.name());
        }

        return namedParameterJdbcTemplate.query(sql, sqlParameters, simpleWordInfoRowMapper);
    }
}
