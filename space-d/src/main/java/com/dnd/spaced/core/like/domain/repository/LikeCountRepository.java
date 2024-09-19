package com.dnd.spaced.core.like.domain.repository;

import com.dnd.spaced.core.like.infrastructure.dto.response.LikeCountInfoDto;
import java.util.List;
import java.util.Map;

public interface LikeCountRepository {

    List<LikeCountInfoDto> findLikeCountAll();

    Map<Long, Integer> findLikeCountAllBy(Long wordId, List<Object> commentIds);

    void addLikeCount(Long wordId, Long commentId);

    void deleteLikeCount(Long wordId, Long commentId);
}
