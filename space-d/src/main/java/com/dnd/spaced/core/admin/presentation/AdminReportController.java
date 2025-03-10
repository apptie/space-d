package com.dnd.spaced.core.admin.presentation;

import com.dnd.spaced.core.admin.application.AdminReportService;
import com.dnd.spaced.core.admin.application.dto.request.ProcessReportRequest;
import com.dnd.spaced.core.admin.application.dto.request.ReadAllReportSearchRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import com.dnd.spaced.global.resolver.admin.report.ReportPageable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping
    public ResponseEntity<ReportCollectionResponse> readReports(
            ReadAllReportSearchRequest request,
            @ReportPageable Pageable pageable
    ) {
        ReportCollectionResponse response = adminReportService.readReports(request, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reportId}")
    public ResponseEntity<Void> processReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ProcessReportRequest request
    ) {
        adminReportService.processReport(reportId, request);

        return ResponseEntityConst.NO_CONTENT;
    }
}
