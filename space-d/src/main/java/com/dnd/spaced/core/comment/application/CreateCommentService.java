package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.exception.WordNotFoundException;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class CreateCommentService {

    private final WordRepository wordRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createComment(Long accountId, Long wordId, CreateCommentRequest request) {
        validateWordId(wordId);

        persistComment(accountId, wordId, request);
    }

    private void validateWordId(Long wordId) {
        if (!wordRepository.existsBy(wordId)) {
            throw new WordNotFoundException("댓글과 관련된 용어를 찾을 수 없습니다.");
        }
    }

    private void persistComment(Long accountId, Long wordId, CreateCommentRequest request) {
        Comment comment = new Comment(accountId, wordId, request.content());

        commentRepository.save(comment);
    }
}
