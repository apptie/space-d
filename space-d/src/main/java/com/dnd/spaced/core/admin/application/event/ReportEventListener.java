package com.dnd.spaced.core.admin.application.event;

import com.dnd.spaced.core.admin.application.event.dto.ProcessedReportEvent;
import com.dnd.spaced.core.admin.application.event.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReportEventListener {

    private final CommentRepository commentRepository;

    @EventListener
    @Transactional
    public void processReport(ProcessedReportEvent event) {
        Comment comment = commentRepository.findBy(event.commentId())
                                           .orElseThrow(() -> new CommentNotFoundException("지정한 댓글을 찾을 수 없습니다."));
        ReportStatus reportStatus = event.reportStatus();

        if (reportStatus.isProcess()) {
            comment.delete();
            return;
        }

        comment.recover();
    }
}
