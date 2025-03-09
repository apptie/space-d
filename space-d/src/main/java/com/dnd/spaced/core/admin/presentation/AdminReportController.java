package com.dnd.spaced.core.admin.presentation;

import com.dnd.spaced.core.admin.application.AdminReportService;
import com.dnd.spaced.core.admin.application.dto.request.ProcessReportRequest;
import com.dnd.spaced.core.admin.application.dto.request.ReadAllReportSearchRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ReportCollectionResponse> findAllBy(ReadAllReportSearchRequest request) {
        ReportCollectionResponse response = adminReportService.findAllBy(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reportId}")
    public ResponseEntity<Void> processReport(
            @PathVariable Long reportId,
            @RequestBody @Valid ProcessReportRequest request
    ) {
        adminReportService.process(reportId, request);

        return ResponseEntityConst.NO_CONTENT;
    }
}
