package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.repository.QuizOptionRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizOptionGatewayRepository implements QuizOptionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<QuizOption> quizOptions) {
        String sql = """
                INSERT INTO quiz_options(content, option_order, word_id, quiz_question_id)
                VALUES(?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                QuizOption quizOption = quizOptions.get(i);

                ps.setString(1, quizOption.getContent());
                ps.setInt(2, quizOption.getOptionOrder());
                ps.setLong(3, quizOption.getWordId());
                ps.setLong(4, quizOption.getQuizQuestionId());
            }

            @Override
            public int getBatchSize() {
                return quizOptions.size();
            }
        });
    }
}
