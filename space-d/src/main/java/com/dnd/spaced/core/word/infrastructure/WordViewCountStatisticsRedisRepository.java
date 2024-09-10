package com.dnd.spaced.core.word.infrastructure;

import com.dnd.spaced.core.word.domain.repository.WordViewCountStatisticsRepository;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.dnd.spaced.core.word.domain.repository.dto.response.ViewCountStatisticsRankDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordViewCountStatisticsRedisRepository implements WordViewCountStatisticsRepository {

    private static final String KEY_PREFIX = "viewCount:";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(Long wordId, LocalDateTime localDateTime) {
        redisTemplate.opsForZSet()
                     .incrementScore(calculateKey(localDateTime), String.valueOf(wordId), 1.0);
    }

    @Override
    public List<WordViewCountStatisticsDto> findAllBy(List<Long> ids, LocalDateTime localDateTime) {
        List<WordViewCountStatisticsDto> result = new ArrayList<>();

        for (Long id : ids) {
            Double score = redisTemplate.opsForZSet()
                                        .score(calculateKey(localDateTime), String.valueOf(id));

            result.add(new WordViewCountStatisticsDto(id, score.longValue()));
        }

        return result;
    }

    @Override
    public List<ViewCountStatisticsRankDto> findByRanking(LocalDateTime localDateTime) {
        Set<TypedTuple<String>> popularWords = redisTemplate.opsForZSet()
                                                            .reverseRangeWithScores(calculateKey(localDateTime), 0, 9);

        List<ViewCountStatisticsRankDto> result = new ArrayList<>();
        int rank = 1;

        for (TypedTuple<String> popularWord : popularWords) {
            result.add(new ViewCountStatisticsRankDto(
                    rank++,
                    Long.valueOf(popularWord.getValue()),
                    popularWord.getScore().longValue())
            );
        }

        return result;
    }

    @Override
    public void deleteAll(LocalDateTime localDateTime) {
        redisTemplate.delete(calculateKey(localDateTime));
    }

    private String calculateKey(LocalDateTime localDateTime) {
        return KEY_PREFIX + FORMATTER.format(localDateTime);
    }
}
