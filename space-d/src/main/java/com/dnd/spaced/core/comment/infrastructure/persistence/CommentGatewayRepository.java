package com.dnd.spaced.core.comment.infrastructure.persistence;

import static com.dnd.spaced.core.account.domain.QAccount.account;
import static com.dnd.spaced.core.comment.domain.QComment.comment;
import static com.dnd.spaced.core.like.domain.QLike.like;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.comment.domain.dto.LikedCommentInfo;
import com.dnd.spaced.global.consts.AuthConst;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentGatewayRepository implements CommentRepository {

    private final JPAQueryFactory queryFactory;
    private final CommentCrudRepository commentCrudRepository;

    @Override
    public Comment save(Comment comment) {
        return commentCrudRepository.save(comment);
    }

    @Override
    public Optional<Comment> findBy(Long commentId) {
        return commentCrudRepository.findById(commentId);
    }

    @Override
    public List<LikedCommentInfo> findAllBy(Long accountId, Long wordId, Long lastCommentId, Pageable pageable) {
        if (AuthConst.GUEST_ACCOUNT_ID.equals(accountId)) {
            return findAllWithoutIsLikedBy(wordId, lastCommentId, pageable);
        }

        return findAllWithIsLikedBy(accountId, wordId, lastCommentId, pageable);
    }

    @Override
    public void delete(Comment comment) {
        commentCrudRepository.delete(comment);
    }

    @Override
    public void increaseLikeCount(Long commentId) {
        queryFactory.update(comment)
                    .set(comment.likeCount, comment.likeCount.add(1))
                    .where(comment.id.eq(commentId))
                    .execute();
    }

    @Override
    public void decreaseLikeCount(Long commentId) {
        queryFactory.update(comment)
                    .set(comment.likeCount, comment.likeCount.subtract(1))
                    .where(comment.id.eq(commentId))
                    .execute();
    }

    private List<LikedCommentInfo> findAllWithIsLikedBy(
            Long accountId,
            Long wordId,
            Long lastCommentId,
            Pageable pageable
    ) {
        return queryFactory.select(getLikedCommentInfoWithLiked())
                           .from(comment)
                           .leftJoin(account).on(comment.writerId.eq(account.id))
                           .leftJoin(like).on(comment.id.eq(like.commentId), like.accountId.eq(accountId))
                           .where(
                                   comment.wordId.eq(wordId),
                                   comment.deleted.isFalse(),
                                   calculateLastIdExpression(lastCommentId)
                           )
                           .orderBy(comment.id.asc())
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    private List<LikedCommentInfo> findAllWithoutIsLikedBy(Long wordId, Long lastCommentId, Pageable pageable) {
        return queryFactory.select(getLikedCommentInfoWithoutLiked())
                           .from(comment)
                           .leftJoin(account).on(comment.writerId.eq(account.id))
                           .where(
                                   comment.wordId.eq(wordId),
                                   comment.deleted.isFalse(),
                                   calculateLastIdExpression(lastCommentId)
                           )
                           .orderBy(comment.id.asc())
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    private BooleanExpression calculateLastIdExpression(Long lastCommentId) {
        if (lastCommentId == null) {
            return null;
        }

        return gtLastCommentId(lastCommentId);
    }

    private BooleanExpression gtLastCommentId(Long commentId) {
        if (commentId == null) {
            return null;
        }

        return comment.id.gt(commentId);
    }

    private ConstructorExpression<LikedCommentInfo> getLikedCommentInfoWithLiked() {
        return Projections.constructor(
                LikedCommentInfo.class,
                comment,
                like.id.isNotNull(),
                account.profile.nickname,
                account.profile.profileImage
        );
    }

    private ConstructorExpression<LikedCommentInfo> getLikedCommentInfoWithoutLiked() {
        return Projections.constructor(
                LikedCommentInfo.class,
                comment,
                account.profile.nickname,
                account.profile.profileImage
        );
    }
}
