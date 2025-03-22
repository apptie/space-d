package com.dnd.spaced.core.quiz.infrastructure.persistence;

import static com.dnd.spaced.core.quiz.domain.QQuizGradedAnswer.quizGradedAnswer;

import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.QuizGradedAnswerRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizGradedAnswerGatewayRepository implements QuizGradedAnswerRepository {

    private final Clock clock;
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public void saveAll(List<QuizGradedAnswer> quizGradedAnswers) {
        String sql = """
                INSERT INTO quiz_graded_answers(created_at, account_id, quiz_id, quiz_question_id, selected_word_id)
                VALUES(?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                QuizGradedAnswer quizGradedAnswer = quizGradedAnswers.get(i);

                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(clock)));
                ps.setLong(2, quizGradedAnswer.getAccountId());
                ps.setLong(3, quizGradedAnswer.getQuizId());
                ps.setLong(4, quizGradedAnswer.getQuizQuestion().getId());
                ps.setLong(5, quizGradedAnswer.getSelectedWordId());
            }

            @Override
            public int getBatchSize() {
                return quizGradedAnswers.size();
            }
        });
    }

    @Override
    public List<QuizGradedAnswer> findAllBy(Long accountId, Long lastGradedAnswerId, Pageable pageable) {
        return queryFactory.selectFrom(quizGradedAnswer)
                           .where(quizGradedAnswer.accountId.eq(accountId), gtLastGradedAnswerId(lastGradedAnswerId))
                           .leftJoin(quizGradedAnswer.quizQuestion).fetchJoin()
                           .orderBy(quizGradedAnswer.id.asc())
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    @Override
    public List<QuizGradedAnswer> findAllBy(Long accountId, Long quizId) {
        return queryFactory.selectFrom(quizGradedAnswer)
                           .where(quizGradedAnswer.quizId.eq(quizId), quizGradedAnswer.accountId.eq(accountId))
                           .leftJoin(quizGradedAnswer.quizQuestion).fetchJoin()
                           .orderBy(quizGradedAnswer.id.asc())
                           .fetch();
    }

    private BooleanExpression gtLastGradedAnswerId(Long lastGradedAnswerId) {
        if (lastGradedAnswerId == null) {
            return null;
        }

        return quizGradedAnswer.id.gt(lastGradedAnswerId);
    }
}
