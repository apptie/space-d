package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto;
import com.dnd.spaced.core.comment.application.exception.AssociationAccountNotFoundException;
import com.dnd.spaced.core.comment.application.exception.AssociationWordNotFoundException;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.like.domain.repository.LikeCountRepository;
import com.dnd.spaced.core.comment.domain.repository.dto.request.CommentPageRequest;
import com.dnd.spaced.core.comment.domain.repository.dto.response.LikedCommentDto;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private static final String NOT_AUTHENTICATION_ACCOUNT = "notAuthenticationAccount";

    private final WordRepository wordRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;
    private final LikeCountRepository likeCountRepository;

    @Transactional
    public void save(String accountId, Long wordId, String content) {
        Account writer = findAccount(accountId);
        Word word = findWord(wordId);
        Comment comment = new Comment(writer.getId(), word.getId(), content);

        commentRepository.save(comment);
    }

    @Transactional
    public void delete(String accountId, Long commentId) {
        Account owner = findAccount(accountId);
        Comment comment = findComment(commentId);

        if (comment.isNotWriter(owner)) {
            throw new ForbiddenCommentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void update(String accountId, Long commentId, String content) {
        Account owner = findAccount(accountId);
        Comment comment = findComment(commentId);

        if (comment.isNotWriter(owner)) {
            throw new ForbiddenCommentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.changeContent(content);
    }

    public List<ReadAllCommentDto> readAllBy(String accountId, Long wordId, Long lastCommentId, Pageable pageable) {
        CommentPageRequest commentPageRequest = new CommentPageRequest(pageable, lastCommentId);
        List<LikedCommentDto> result = commentRepository.findAllBy(
                calculateLikeAccountId(accountId),
                wordId,
                commentPageRequest
        );
        List<Object> commentIds = result.stream()
                                        .map(dto -> (Object) dto.comment().getId())
                                        .toList();
        Map<Long, Integer> cacheLikeCount = likeCountRepository.findLikeCountAllBy(wordId, commentIds);

        return result.stream()
                     .map(dto -> ReadAllCommentDto.of(dto, cacheLikeCount))
                     .toList();
    }

    private Account findAccount(String accountId) {
        return accountRepository.findBy(accountId)
                                .orElseThrow(() -> new AssociationAccountNotFoundException("유효하지 않은 회원입니다."));
    }

    private Word findWord(Long wordId) {
        return wordRepository.findBy(wordId)
                             .orElseThrow(() -> new AssociationWordNotFoundException("댓글과 관련된 용어를 찾을 수 없습니다."));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findBy(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("지정한 ID에 해당하는 댓글이 없습니다."));
    }

    private String calculateLikeAccountId(String accountId) {
        if (accountId == null) {
            return NOT_AUTHENTICATION_ACCOUNT;
        }

        return accountId;
    }
}
