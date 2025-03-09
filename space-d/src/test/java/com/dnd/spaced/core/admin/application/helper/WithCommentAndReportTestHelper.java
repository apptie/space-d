package com.dnd.spaced.core.admin.application.helper;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.report.domain.Report;
import com.dnd.spaced.core.report.domain.enums.ReportReason;
import com.dnd.spaced.core.report.domain.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class WithCommentAndReportTestHelper {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReportRepository reportRepository;

    protected Report report;
    protected Comment comment;

    @BeforeEach
    void beforeEach() {
        comment = new Comment(1L, 1L, "친구초대 특별이벤트 링크 : ");
        commentRepository.save(comment);

        report = new Report(ReportReason.SPAM, comment.getId(), 2L);
        reportRepository.save(report);
    }
}
