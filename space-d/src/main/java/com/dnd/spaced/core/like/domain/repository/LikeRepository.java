package com.dnd.spaced.core.like.domain.repository;

import com.dnd.spaced.core.like.domain.Like;
import java.util.Optional;

public interface LikeRepository {

    void save(Like like);

    void delete(Like like);

    Optional<Like> findBy(String accountId, Long commentId);
}
