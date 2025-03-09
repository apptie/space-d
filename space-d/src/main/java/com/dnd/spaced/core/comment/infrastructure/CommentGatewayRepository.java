package com.dnd.spaced.core.comment.infrastructure;

import static com.dnd.spaced.core.account.domain.QAccount.account;
import static com.dnd.spaced.core.comment.domain.QComment.comment;
import static com.dnd.spaced.core.like.domain.QLike.like;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.comment.domain.repository.dto.response.LikedCommentDto;
import com.dnd.spaced.core.comment.infrastructure.util.CommentSortConditionConverter;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
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

    private static final String COMMENT_ID = "id";
    private static final int TUPLE_COMMENT_INDEX = 0;
    private static final int TUPLE_WRITER_NICKNAME_INDEX = 1;
    private static final int TUPLE_WRITER_PROFILE_IMAGE_INDEX = 2;
    private static final int TUPLE_WRITER_ID_INDEX = 3;

    private final JPAQueryFactory queryFactory;
    private final CommentCrudRepository commentCrudRepository;

    @Override
    public Comment save(Comment comment) {
        return commentCrudRepository.save(comment);
    }

    @Override
    public Optional<Comment> findBy(Long id) {
        return commentCrudRepository.findById(id);
    }

    @Override
    public List<LikedCommentDto> findAllBy(Long accountId, Long wordId, Long lastCommentId, Pageable pageable) {
        if (accountId == null) {
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

    private List<LikedCommentDto> findAllWithIsLikedBy(
            Long accountId,
            Long wordId,
            Long lastCommentId,
            Pageable pageable
    ) {
        return queryFactory.select(
                                   Projections.constructor(
                                           LikedCommentDto.class,
                                           comment,
                                           like.id.isNotNull(),
                                           account.profileInfo.nickname,
                                           account.profileInfo.profileImage,
                                           account.id
                                   )
                           )
                           .from(comment)
                           .join(account).on(comment.accountId.eq(account.id))
                           .leftJoin(like).on(comment.id.eq(like.commentId), like.accountId.eq(accountId))
                           .where(
                                   comment.wordId.eq(wordId),
                                   calculateLastIdExpression(lastCommentId, pageable),
                                   comment.isDeleted.isFalse()
                           )
                           .orderBy(
                                   CommentSortConditionConverter.convert(pageable)
                                                                .toArray(OrderSpecifier[]::new)
                           )
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    private List<LikedCommentDto> findAllWithoutIsLikedBy(Long wordId, Long lastCommentId, Pageable pageable) {
        List<Tuple> comments = queryFactory.select(
                                                   comment,
                                                   account.profileInfo.nickname,
                                                   account.profileInfo.profileImage,
                                                   account.id
                                           )
                                           .from(comment)
                                           .join(account).on(comment.accountId.eq(account.id))
                                           .where(
                                                   comment.wordId.eq(wordId),
                                                   calculateLastIdExpression(lastCommentId, pageable),
                                                   comment.isDeleted.isFalse()
                                           )
                                           .orderBy(
                                                   CommentSortConditionConverter.convert(pageable)
                                                                                .toArray(OrderSpecifier[]::new)
                                           )
                                           .limit(pageable.getPageSize())
                                           .fetch();

        return comments.stream()
                       .map(this::convertLikedCommentDto)
                       .toList();
    }

    private LikedCommentDto convertLikedCommentDto(Tuple tuple) {
        return new LikedCommentDto(
                tuple.get(TUPLE_COMMENT_INDEX, Comment.class),
                false,
                tuple.get(TUPLE_WRITER_NICKNAME_INDEX, String.class),
                tuple.get(TUPLE_WRITER_PROFILE_IMAGE_INDEX, String.class),
                tuple.get(TUPLE_WRITER_ID_INDEX, Long.class)
        );
    }

    private BooleanExpression calculateLastIdExpression(Long lastCommentId, Pageable pageable) {
        if (lastCommentId == null) {
            return null;
        }

        if (isAscending(pageable)) {
            return gtLastCommentId(lastCommentId);
        }

        return ltLastCommentId(lastCommentId);
    }

    private boolean isAscending(Pageable pageable) {
        return pageable.getSort()
                       .get()
                       .anyMatch(order -> COMMENT_ID.equals(order.getProperty()) && order.isAscending());
    }

    private BooleanExpression gtLastCommentId(Long commentId) {
        if (commentId == null) {
            return null;
        }

        return comment.id.gt(commentId);
    }

    private BooleanExpression ltLastCommentId(Long commentId) {
        if (commentId == null) {
            return null;
        }

        return comment.id.lt(commentId);
    }
}
