package com.dnd.spaced.core.like.application.schedule;

import com.dnd.spaced.core.like.domain.repository.LikeCountRepository;
import com.dnd.spaced.core.like.infrastructure.dto.response.LikeCountInfoDto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeCountScheduler {

    private final JdbcTemplate jdbcTemplate;
    private final LikeCountRepository likeCountRepository;

    @Scheduled(cron = "0 */30 * * * *")
    @Transactional
    public void schedule() {
        List<LikeCountInfoDto> likeCountInfos = likeCountRepository.findLikeCountAll();

        jdbcTemplate.batchUpdate(
                "UPDATE comments c SET c.like_count =: likeCount WHERE c.id =: id",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        LikeCountInfoDto likeCountInfoDto = likeCountInfos.get(i);

                        ps.setInt(1, likeCountInfoDto.likeCount());
                        ps.setLong(2, likeCountInfoDto.commentId());
                    }

                    @Override
                    public int getBatchSize() {
                        return 100;
                    }
                });
    }
}
