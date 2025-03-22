package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.repository.QuizQuestionRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizQuestionGatewayRepository implements QuizQuestionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> saveAll(List<QuizQuestion> quizQuestions) {
        if (quizQuestions.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = """
                INSERT INTO quiz_questions(question, passage, answer_content, answer_word_id, quiz_category, quiz_id)
                VALUES(?, ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.batchUpdate(
                conn -> conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS),
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        QuizQuestion quizQuestion = quizQuestions.get(i);

                        ps.setString(1, quizQuestion.getQuestion());
                        ps.setString(2, quizQuestion.getPassage());
                        ps.setString(3, quizQuestion.getQuizAnswerOption().getAnswerContent());
                        ps.setLong(4, quizQuestion.getQuizAnswerOption().getAnswerWordId());
                        ps.setString(5, quizQuestion.getQuizCategory().name());
                        ps.setLong(6, quizQuestion.getQuiz().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return quizQuestions.size();
                    }
                },
                keyHolder
        );

        List<Long> quizQuestionIds = new ArrayList<>();
        List<Map<String, Object>> keyHolders = keyHolder.getKeyList();

        for (Map<String, Object> keyMap : keyHolders) {
            Number key = (Number) keyMap.values()
                                        .iterator()
                                        .next();

            quizQuestionIds.add(key.longValue());
        }

        return quizQuestionIds;
    }
}
