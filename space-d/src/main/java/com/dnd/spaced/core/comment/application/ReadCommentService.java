package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.comment.application.dto.mapper.CommentResponseCollectionMapper;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
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
    private final CommentResponseCollectionMapper mapper;

    public CommentCollectionResponse readComments(Long accountId, Long wordId, Long lastCommentId, Pageable pageable) {
        List<LikedComment> comments = commentRepository.findAllBy(accountId, wordId, lastCommentId, pageable);

        return mapper.toDto(comments);
    }
}
