package com.dnd.spaced.core.report.application.helper;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class WithCommentTestHelper {

    @Autowired
    CommentRepository commentRepository;

    protected Comment comment;

    @BeforeEach
    void beforeEach() {
        comment = new Comment(12L, 1L, "친구초대 특별이벤트 링크 : ");
        commentRepository.save(comment);
    }
}
