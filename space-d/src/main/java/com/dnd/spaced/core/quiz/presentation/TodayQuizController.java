package com.dnd.spaced.core.quiz.presentation;

import com.dnd.spaced.core.quiz.application.TodayQuizService;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfo;
import com.dnd.spaced.global.resolver.quiz.GradedAnswerPageable;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/today-quizzes")
@RequiredArgsConstructor
public class TodayQuizController {

    private final TodayQuizService todayQuizService;

    @GetMapping("/latest")
    public ResponseEntity<TodayQuizResponse> findLatest() {
        return ResponseEntity.ok(todayQuizService.findLatest());
    }

    @GetMapping("/{todayQuizId}")
    public ResponseEntity<TodayQuizResponse> findBy(@PathVariable Long todayQuizId) {
        return ResponseEntity.ok(
                todayQuizService.findBy(todayQuizId)
        );
    }

    @PostMapping("/{todayQuizId}/graded-answers")
    public ResponseEntity<Void> grade(
            @AuthAccount AuthAccountInfo accountInfo,
            @PathVariable Long todayQuizId,
            @Valid @RequestBody GradeTodayQuizRequest request
    ) {
        todayQuizService.grade(accountInfo.accountId(), todayQuizId, request);
        URI location = UriComponentsBuilder.fromPath("/today-quizzes/{id}/graded-answers")
                                           .buildAndExpand(todayQuizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @GetMapping("/{todayQuizId}/graded-answers")
    public ResponseEntity<TodayQuizGradedAnswerResponse> findTodayQuizGradedAnswerBy(
            @AuthAccount AuthAccountInfo accountInfo,
            @PathVariable Long todayQuizId
    ) {
        return ResponseEntity.ok(todayQuizService.findTodayQuizGradedAnswerBy(accountInfo.accountId(), todayQuizId));
    }

    @GetMapping("/graded-answers")
    public ResponseEntity<TodayQuizGradedAnswerCollectionResponse> findTodayQuizGradedAnswerAllBy(
            @AuthAccount AuthAccountInfo accountInfo,
            ReadTodayQuizGradedAnswerSearchRequest request,
            @GradedAnswerPageable Pageable pageable
    ) {
        return ResponseEntity.ok(
                todayQuizService.findTodayQuizGradedAnswerAllBy(accountInfo.accountId(), request, pageable)
        );
    }
}
