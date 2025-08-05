package com.dnd.spaced.core.word.infrastructure.persistence;

import com.dnd.spaced.core.word.application.repository.DeletedWordIdRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeletedWordGatewayIdRepository implements DeletedWordIdRepository {

    private static final String KEY = "word::deleted";
    private static final double SCORE_OFFSET = 0.000001d;

    private final RedisTemplate<String, Long> deletedWordIdRedisTemplate;

    @Override
    public void save(Long wordId, LocalDateTime targetTime) {
        double targetScore = calculateTargetScore(targetTime);

        deletedWordIdRedisTemplate.opsForZSet()
                                  .add(KEY, wordId, targetScore);
    }

    @Override
    public Set<Long> findAllBy(LocalDateTime targetTime) {
        double targetScore = calculateTargetScore(targetTime);

        return deletedWordIdRedisTemplate.opsForZSet()
                                         .rangeByScore(KEY, Double.NEGATIVE_INFINITY, targetScore);
    }

    @Override
    public void deleteAllBy(LocalDateTime targetTime) {
        double targetScore = calculateTargetScore(targetTime);

        deletedWordIdRedisTemplate.opsForZSet().removeRangeByScore(
                KEY,
                Double.NEGATIVE_INFINITY,
                targetScore + SCORE_OFFSET
        );
    }

    private double calculateTargetScore(LocalDateTime targetTime) {
        return targetTime.atZone(ZoneId.systemDefault())
                         .toEpochSecond();
    }
}
