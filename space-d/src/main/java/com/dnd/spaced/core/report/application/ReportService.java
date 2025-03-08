package com.dnd.spaced.core.report.application;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.report.application.dto.request.ReportRequest;
import com.dnd.spaced.core.report.application.exception.CannotReportOwnCommentException;
import com.dnd.spaced.core.report.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.report.application.exception.ReportReasonNotFoundException;
import com.dnd.spaced.core.report.domain.Report;
import com.dnd.spaced.core.report.domain.enums.ReportReason;
import com.dnd.spaced.core.report.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void report(Long reporterId, ReportRequest request) {
        commentRepository.findBy(request.commentId())
                         .ifPresentOrElse(
                                 comment -> processReport(comment, reporterId, request),
                                 () -> {
                                     throw new CommentNotFoundException("신고하려는 댓글을 찾을 수 없습니다.");
                                 }
                         );
    }

    private void processReport(Comment comment, Long reporterId, ReportRequest request) {
        validateReportedComment(comment, reporterId);

        ReportReason reportReason = ReportReason.findBy(request.cause())
                                                .orElseThrow(
                                                        () -> new ReportReasonNotFoundException(
                                                                "지정한 원인의 신고 사유를 찾지 못했습니다."
                                                        )
                                                );
        Report report = new Report(reportReason, request.commentId(), reporterId);

        reportRepository.save(report);
    }

    private void validateReportedComment(Comment comment, Long reporterId) {
        if (comment.isWriter(reporterId)) {
            throw new CannotReportOwnCommentException("자신이 작성한 댓글은 신고할 수 없습니다.");
        }
    }
}
