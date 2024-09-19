package com.dnd.spaced.core.like.infrastructure;

import com.dnd.spaced.core.like.infrastructure.dto.LikeCountIdentifier;
import com.dnd.spaced.core.like.domain.repository.LikeCountRepository;
import com.dnd.spaced.core.like.infrastructure.dto.response.LikeCountInfoDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

@Repository
public class LikeCountRedisRepository implements LikeCountRepository {

    private static final String KEY = "likeCount:";

    private final RedisTemplate<String, String> redisTemplate;
    private final LikeCountBuffer buffer;

    public LikeCountRedisRepository(
            RedisTemplate<String, String> redisTemplate,
            Executor asyncCommentLikeCountExecutor
    ) {
        this.redisTemplate = redisTemplate;
        this.buffer = new LikeCountBuffer(this::updateCache, asyncCommentLikeCountExecutor);
    }

    @Override
    public List<LikeCountInfoDto> findLikeCountAll() {
        List<LikeCountInfoDto> result = new ArrayList<>();
        ScanOptions options = ScanOptions.scanOptions()
                                         .match(KEY + "*")
                                         .count(10)
                                         .build();
        Cursor<String> cursor = redisTemplate.scan(options);

        while (cursor.hasNext()) {
            String key = cursor.next();
            redisTemplate.opsForHash()
                         .entries(key)
                         .forEach(
                                 (member, value) -> result.add(
                                         new LikeCountInfoDto(
                                                 Long.parseLong((String) member),
                                                 Integer.parseInt((String) value)
                                         )
                                 )
                         );
        }

        return result;
    }

    @Override
    public Map<Long, Integer> findLikeCountAllBy(Long wordId, List<Object> commentIds) {
        List<Object> objects = redisTemplate.opsForHash()
                                            .multiGet(calculateKey(wordId), commentIds);
        Map<Long, Integer> result = new HashMap<>();

        for (int i = 0; i < objects.size(); i++) {
            result.put((Long) commentIds.get(i), (Integer) objects.get(i));
        }

        return result;
    }

    @Override
    public void addLikeCount(Long wordId, Long commentId) {
        buffer.addLikeCount(new LikeCountIdentifier(wordId, commentId));
    }

    @Override
    public void deleteLikeCount(Long wordId, Long commentId) {
        buffer.deleteLikeCount(new LikeCountIdentifier(wordId, commentId));
    }

    private String calculateKey(Long wordId) {
        return KEY + wordId;
    }

    private void updateCache(Map<LikeCountIdentifier, Integer> buffer) {
        buffer.forEach(
                (identifier, count) -> redisTemplate.opsForHash()
                                                    .increment(
                                                            calculateKey(identifier.wordId()),
                                                            identifier.commentId(),
                                                            count
                                                    )
        );
    }
}
