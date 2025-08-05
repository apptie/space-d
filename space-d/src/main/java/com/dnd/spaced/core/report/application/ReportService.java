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

    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void report(Long reporterId, ReportRequest request) {
        Comment comment = findComment(request);

        processReport(comment, reporterId, request);
    }

    private Comment findComment(ReportRequest request) {
        return commentRepository.findBy(request.commentId())
                                .orElseThrow(() -> new CommentNotFoundException("신고하려는 댓글을 찾을 수 없습니다."));
    }

    private void processReport(Comment comment, Long reporterId, ReportRequest request) {
        validateSelfReporting(comment, reporterId);

        ReportReason reportReason = findReportReason(request);
        persistReport(reporterId, request, reportReason);
    }

    private void validateSelfReporting(Comment comment, Long reporterId) {
        if (comment.isWriter(reporterId)) {
            throw new CannotReportOwnCommentException("자신이 작성한 댓글은 신고할 수 없습니다.");
        }
    }

    private ReportReason findReportReason(ReportRequest request) {
        return ReportReason.findBy(request.cause())
                           .orElseThrow(
                                   () -> new ReportReasonNotFoundException(
                                           "지정한 원인의 신고 사유를 찾지 못했습니다."
                                   )
                           );
    }

    private void persistReport(Long reporterId, ReportRequest request, ReportReason reportReason) {
        Report report = new Report(reportReason, request.commentId(), reporterId);

        reportRepository.save(report);
    }
}
