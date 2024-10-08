package com.dnd.spaced.core.comment.infrastructure;

import static com.dnd.spaced.core.account.domain.QAccount.account;
import static com.dnd.spaced.core.comment.domain.QComment.comment;
import static com.dnd.spaced.core.like.domain.QLike.like;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.comment.domain.repository.dto.request.CommentPageRequest;
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
public class CommentQuerydslRepository implements CommentRepository {

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
    public List<LikedCommentDto> findAllBy(String accountId, Long wordId, CommentPageRequest pageRequest) {
        if (accountId == null) {
            return findAllWithoutIsLikedBy(wordId, pageRequest);
        }

        return findAllWithIsLikedBy(accountId, wordId, pageRequest);
    }

    @Override
    public void delete(Comment comment) {
        commentCrudRepository.delete(comment);
    }

    private List<LikedCommentDto> findAllWithIsLikedBy(String accountId, Long wordId, CommentPageRequest pageRequest) {
        return queryFactory.select(
                                   Projections.constructor(
                                           LikedCommentDto.class,
                                           comment,
                                           like.id.isNotNull(),
                                           account.profileInfo.nickname,
                                           account.profileInfo.profileImage
                                   )
                           )
                           .from(comment)
                           .join(account).on(comment.accountId.eq(account.id))
                           .leftJoin(like).on(comment.id.eq(like.commentId), like.accountId.eq(accountId))
                           .where(comment.wordId.eq(wordId), calculateLastCommentIdBooleanExpression(pageRequest))
                           .orderBy(
                                   CommentSortConditionConverter.convert(pageRequest.pageable())
                                                                .toArray(OrderSpecifier[]::new)
                           )
                           .limit(pageRequest.pageable().getPageSize())
                           .fetch();
    }

    private List<LikedCommentDto> findAllWithoutIsLikedBy(Long wordId, CommentPageRequest pageRequest) {
        List<Tuple> comments = queryFactory.select(
                                                   comment,
                                                   account.profileInfo.nickname,
                                                   account.profileInfo.profileImage
                                           )
                                           .from(comment)
                                           .join(account).on(comment.accountId.eq(account.id))
                                           .where(comment.wordId.eq(wordId),
                                                   calculateLastCommentIdBooleanExpression(pageRequest))
                                           .orderBy(
                                                   CommentSortConditionConverter.convert(pageRequest.pageable())
                                                                                .toArray(OrderSpecifier[]::new)
                                           )
                                           .limit(pageRequest.pageable().getPageSize())
                                           .fetch();

        return comments.stream()
                       .map(this::convertLikedCommentDto)
                       .toList();
    }

    private LikedCommentDto convertLikedCommentDto(Tuple tuple) {
        return new LikedCommentDto(
                tuple.get(0, Comment.class),
                false,
                tuple.get(1, String.class),
                tuple.get(2, String.class)
        );
    }

    private BooleanExpression calculateLastCommentIdBooleanExpression(CommentPageRequest pageRequest) {
        if (pageRequest.lastCommentId() == null) {
            return null;
        }

        if (isLastCommentIdGt(pageRequest.pageable())) {
            return lastCommentIdGt(pageRequest.lastCommentId());
        }

        return lastCommentIdLt(pageRequest.lastCommentId());
    }

    private boolean isLastCommentIdGt(Pageable pageable) {
        return pageable.getSort()
                       .get()
                       .anyMatch(order -> "id".equals(order.getProperty()) && order.isAscending());
    }

    private BooleanExpression lastCommentIdGt(Long id) {
        if (id == null) {
            return null;
        }

        return comment.id.gt(id);
    }

    private BooleanExpression lastCommentIdLt(Long id) {
        if (id == null) {
            return null;
        }

        return comment.id.lt(id);
    }
}
