package com.dnd.spaced.core.like.infrastructure.persistence;

import static com.dnd.spaced.core.like.domain.QLike.like;

import com.dnd.spaced.core.like.domain.Like;
import com.dnd.spaced.core.like.domain.repository.LikeRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeGatewayRepository implements LikeRepository {

    private final JPAQueryFactory queryFactory;
    private final LikeCrudRepository likeCrudRepository;

    @Override
    public void save(Like like) {
        likeCrudRepository.save(like);
    }

    @Override
    public void delete(Like like) {
        likeCrudRepository.delete(like);
    }

    @Override
    public Optional<Like> findBy(Long accountId, Long commentId) {
        Like result = queryFactory.selectFrom(like)
                                  .where(like.accountId.eq(accountId), like.commentId.eq(commentId))
                                  .fetchOne();

        return Optional.ofNullable(result);
    }
}
