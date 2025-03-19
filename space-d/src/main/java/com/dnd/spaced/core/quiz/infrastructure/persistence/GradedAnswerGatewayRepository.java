package com.dnd.spaced.core.quiz.infrastructure.persistence;

import static com.dnd.spaced.core.quiz.domain.QGradedAnswer.gradedAnswer;

import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.GradedAnswerRepository;
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
public class GradedAnswerGatewayRepository implements GradedAnswerRepository {

    private final Clock clock;
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public void saveAll(List<GradedAnswer> gradedAnswers) {
        String sql = """
                INSERT INTO graded_answer(created_at, account_id, quiz_id, quiz_question_id, selected_word_id)
                VALUES(?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                GradedAnswer gradedAnswer = gradedAnswers.get(i);

                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(clock)));
                ps.setLong(2, gradedAnswer.getAccountId());
                ps.setLong(3, gradedAnswer.getQuizId());
                ps.setLong(4, gradedAnswer.getQuizQuestion().getId());
                ps.setLong(5, gradedAnswer.getSelectedWordId());
            }

            @Override
            public int getBatchSize() {
                return gradedAnswers.size();
            }
        });
    }

    @Override
    public List<GradedAnswer> findAllBy(Long accountId, Long lastGradedAnswerId, Pageable pageable) {
        return queryFactory.selectFrom(gradedAnswer)
                           .where(gradedAnswer.accountId.eq(accountId), ltLastGradedAnswerId(lastGradedAnswerId))
                           .leftJoin(gradedAnswer.quizQuestion).fetchJoin()
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    @Override
    public List<GradedAnswer> findAllBy(Long quizId) {
        return queryFactory.selectFrom(gradedAnswer)
                           .where(gradedAnswer.quizId.eq(quizId))
                           .leftJoin(gradedAnswer.quizQuestion).fetchJoin()
                           .fetch();
    }

    private BooleanExpression ltLastGradedAnswerId(Long lastGradedAnswerId) {
        if (lastGradedAnswerId == null) {
            return null;
        }

        return gradedAnswer.id.lt(lastGradedAnswerId);
    }
}
