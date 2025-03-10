package com.dnd.spaced.core.report.presentation;

import com.dnd.spaced.core.report.application.ReportService;
import com.dnd.spaced.core.report.application.dto.request.ReportRequest;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfo;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> report(
            @AuthAccount AuthAccountInfo accountInfo,
            @Valid @RequestBody ReportRequest request
    ) {
        reportService.report(accountInfo.accountId(), request);

        return ResponseEntityConst.NO_CONTENT;
    }
}
