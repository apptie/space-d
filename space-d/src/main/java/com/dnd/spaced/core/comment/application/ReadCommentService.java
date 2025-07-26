package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.comment.domain.dto.LikedComment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReadCommentService {

    private final CommentRepository commentRepository;

    public List<LikedComment> readComments(Long accountId, Long wordId, Long lastCommentId, Pageable pageable) {
        return commentRepository.findAllBy(accountId, wordId, lastCommentId, pageable);
    }
}
