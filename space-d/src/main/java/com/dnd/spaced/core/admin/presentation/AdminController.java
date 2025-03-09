package com.dnd.spaced.core.admin.presentation;

import com.dnd.spaced.core.admin.application.AdminReportService;
import com.dnd.spaced.core.admin.application.AdminTodayQuizService;
import com.dnd.spaced.core.admin.application.AdminWordService;
import com.dnd.spaced.core.admin.application.dto.request.ProcessReportRequest;
import com.dnd.spaced.core.admin.application.dto.request.ReadAllReportSearchRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.core.admin.application.dto.request.UpdateBlacklistTokenRequest;
import com.dnd.spaced.core.admin.application.dto.request.UpdateWordExampleRequest;
import com.dnd.spaced.core.auth.application.BlacklistTokenService;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminWordService adminWordService;
    private final BlacklistTokenService blacklistTokenService;
    private final AdminTodayQuizService adminTodayQuizService;
    private final AdminReportService adminReportService;

    @PostMapping("/blacklist-token")
    public ResponseEntity<Void> registerBlacklistToken(@Valid @RequestBody UpdateBlacklistTokenRequest request) {
        blacklistTokenService.register(request.accountId());

        return ResponseEntityConst.CREATED_DEFAULT_CONTEXT_PATH;
    }

    @PostMapping("/words")
    public ResponseEntity<Void> createWord(@Valid @RequestBody CreateWordRequest request) {
        Long wordId = adminWordService.createWord(request);
        URI location = UriComponentsBuilder.fromPath("/words/{wordId}")
                                           .buildAndExpand(wordId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @PatchMapping("/words/examples/{id}")
    public ResponseEntity<Void> updateWordExample(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWordExampleRequest request
    ) {
        adminWordService.updateWordExample(id, request.example());

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping("/words/{wordId}/examples/{exampleId}")
    public ResponseEntity<Void> deleteWordExample(@PathVariable Long wordId, @PathVariable Long exampleId) {
        adminWordService.deleteWordExample(wordId, exampleId);

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping("/words/{wordId}/pronunciations/{pronunciationId}")
    public ResponseEntity<Void> deletePronunciation(@PathVariable Long wordId, @PathVariable Long pronunciationId) {
        adminWordService.deletePronunciation(wordId, pronunciationId);

        return ResponseEntityConst.NO_CONTENT;
    }

    @PostMapping("/today-quizzes")
    public ResponseEntity<Void> createTodayQuiz() {
        Long todayQuizId = adminTodayQuizService.create();
        URI location = UriComponentsBuilder.fromPath("/today-quizzes/{todayQuizId}")
                                           .buildAndExpand(todayQuizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @GetMapping("/reports")
    public ResponseEntity<ReportCollectionResponse> findAllBy(ReadAllReportSearchRequest request) {
        ReportCollectionResponse response = adminReportService.findAllBy(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reports/{reportId}")
    public ResponseEntity<Void> processReport(
            @PathVariable Long reportId,
            @RequestBody @Valid ProcessReportRequest request
    ) {
        adminReportService.process(reportId, request);

        return ResponseEntityConst.NO_CONTENT;
    }
}
