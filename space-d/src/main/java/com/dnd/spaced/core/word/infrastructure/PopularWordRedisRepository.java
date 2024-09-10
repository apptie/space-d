package com.dnd.spaced.core.word.infrastructure;

import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.PopularWordInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PopularWordRedisRepository implements PopularWordRepository {

    private static final String KEY_PREFIX = "popular:info:";
    private static final String CACHE_KEY_PREFIX = "popular:id:";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final RedisTemplate<String, Long> popularWordIdRedisTemplate;
    private final RedisTemplate<String, PopularWordInfo> popularWordRedisTemplate;

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
    public List<PopularWordInfo> findAllBy(LocalDateTime localDateTime) {
        return popularWordRedisTemplate.opsForList()
                                       .range(calculateKey(KEY_PREFIX, localDateTime), 0, -1);
    }

    @Override
    public void deleteAll(LocalDateTime localDateTime) {
        popularWordRedisTemplate.delete(calculateKey(KEY_PREFIX, localDateTime));
        popularWordIdRedisTemplate.delete(calculateKey(CACHE_KEY_PREFIX, localDateTime));
    }

    @Override
    public void saveAll(List<PopularWordInfo> popularWordInfos, LocalDateTime localDateTime) {
        savePopularWordInfos(popularWordInfos, localDateTime);
        cachePopularWordIds(popularWordInfos, localDateTime);
    }

    private void savePopularWordInfos(List<PopularWordInfo> popularWordInfos, LocalDateTime localDateTime) {
        popularWordRedisTemplate.opsForList()
                                .leftPushAll(calculateKey(KEY_PREFIX, localDateTime), popularWordInfos);
    }

    private void cachePopularWordIds(List<PopularWordInfo> popularWordInfos, LocalDateTime localDateTime) {
        Long[] popularWordIds = popularWordInfos.stream()
                                                .map(PopularWordInfo::wordId)
                                                .toArray(Long[]::new);

        popularWordIdRedisTemplate.opsForSet()
                                  .add(calculateKey(CACHE_KEY_PREFIX, localDateTime), popularWordIds);

    }

    private String calculateKey(String key, LocalDateTime localDateTime) {
        return key + FORMATTER.format(localDateTime);
    }
}
