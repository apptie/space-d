package com.dnd.spaced.core.quiz.presentation;

import com.dnd.spaced.core.quiz.application.TodayQuizService;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.global.auth.resolver.AuthAccountId;
import com.dnd.spaced.global.auth.resolver.CurrentAccount;
import com.dnd.spaced.global.auth.resolver.GuestAccountId;
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
    public ResponseEntity<SimpleTodayQuizResponse> readLatestTodayQuiz() {
        return ResponseEntity.ok(todayQuizService.readLatestTodayQuiz());
    }

    @GetMapping("/{todayQuizId}")
    public ResponseEntity<TodayQuizResponse> readTodayQuiz(
            @CurrentAccount GuestAccountId accountId,
            @PathVariable Long todayQuizId
    ) {
        return ResponseEntity.ok(
                todayQuizService.readTodayQuiz(accountId.id(), todayQuizId)
        );
    }

    @PostMapping("/{todayQuizId}/graded-answers")
    public ResponseEntity<Void> gradeTodayQuiz(
            @CurrentAccount AuthAccountId accountId,
            @PathVariable Long todayQuizId,
            @Valid @RequestBody GradeTodayQuizRequest request
    ) {
        todayQuizService.grade(accountId.id(), todayQuizId, request);
        URI location = UriComponentsBuilder.fromPath("/today-quizzes/{id}/graded-answers")
                                           .buildAndExpand(todayQuizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @GetMapping("/{todayQuizId}/graded-answers")
    public ResponseEntity<TodayQuizGradedAnswerResponse> readTargetTodayQuizGradedAnswers(
            @CurrentAccount AuthAccountId accountId,
            @PathVariable Long todayQuizId
    ) {
        return ResponseEntity.ok(todayQuizService.readTargetTodayQuizGradedAnswers(accountId.id(), todayQuizId));
    }

    @GetMapping("/graded-answers")
    public ResponseEntity<TodayQuizGradedAnswerCollectionResponse> readTodayQuizGradedAnswers(
            @CurrentAccount AuthAccountId accountId,
            ReadTodayQuizGradedAnswerSearchRequest request,
            @GradedAnswerPageable Pageable pageable
    ) {
        return ResponseEntity.ok(
                todayQuizService.readTodayQuizGradedAnswers(accountId.id(), request, pageable)
        );
    }
}
