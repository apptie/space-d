package com.dnd.spaced.core.word.infrastructure.persistence;

import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.PopularWord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PopularWordGatewayRepository implements PopularWordRepository {

    private static final String KEY_PREFIX = "popular:info:";
    private static final String CACHE_KEY_PREFIX = "popular:id:";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final RedisTemplate<String, Long> popularWordIdRedisTemplate;
    private final RedisTemplate<String, PopularWord> popularWordRedisTemplate;

    @Override
    public boolean existsBy(Long wordId, LocalDateTime localDateTime) {
        return Boolean.TRUE.equals(popularWordIdRedisTemplate.opsForSet()
                                                             .isMember(
                                                                     calculateKey(CACHE_KEY_PREFIX, localDateTime),
                                                                     wordId
                                                             )
        );
    }

    @Override
    public List<PopularWord> findAllBy(LocalDateTime localDateTime) {
        return popularWordRedisTemplate.opsForList()
                                       .range(calculateKey(KEY_PREFIX, localDateTime), 0, -1);
    }

    @Override
    public void deleteAll(LocalDateTime localDateTime) {
        popularWordRedisTemplate.delete(calculateKey(KEY_PREFIX, localDateTime));
        popularWordIdRedisTemplate.delete(calculateKey(CACHE_KEY_PREFIX, localDateTime));
    }

    @Override
    public void saveAll(List<PopularWord> popularWords, LocalDateTime localDateTime) {
        savePopularWordInfos(popularWords, localDateTime);
        cachePopularWordIds(popularWords, localDateTime);
    }

    private void savePopularWordInfos(List<PopularWord> popularWords, LocalDateTime localDateTime) {
        popularWordRedisTemplate.opsForList()
                                .leftPushAll(calculateKey(KEY_PREFIX, localDateTime), popularWords);
    }

    private void cachePopularWordIds(List<PopularWord> popularWords, LocalDateTime localDateTime) {
        Long[] popularWordIds = popularWords.stream()
                                            .map(PopularWord::wordId)
                                            .toArray(Long[]::new);

        popularWordIdRedisTemplate.opsForSet()
                                  .add(calculateKey(CACHE_KEY_PREFIX, localDateTime), popularWordIds);

    }

    private String calculateKey(String key, LocalDateTime localDateTime) {
        return key + FORMATTER.format(localDateTime);
    }
}
