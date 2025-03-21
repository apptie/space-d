package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizOptionRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodayQuizOptionGatewayRepository implements TodayQuizOptionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TodayQuizOptionCrudRepository todayQuizOptionCrudRepository;

    @Override
    public TodayQuizOption save(TodayQuizOption todayQuizOption) {
        return todayQuizOptionCrudRepository.save(todayQuizOption);
    }

    @Override
    public void saveAll(List<TodayQuizOption> todayQuizOptions) {
        String sql = """
                INSERT INTO today_quiz_options(content, option_order, word_id, today_quiz_id)
                VALUES(?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                TodayQuizOption todayQuizOption = todayQuizOptions.get(i);

                ps.setString(1, todayQuizOption.getContent());
                ps.setInt(2, todayQuizOption.getOptionOrder());
                ps.setLong(3, todayQuizOption.getWordId());
                ps.setLong(4, todayQuizOption.getTodayQuiz().getId());
            }

            @Override
            public int getBatchSize() {
                return todayQuizOptions.size();
            }
        });
    }
}
