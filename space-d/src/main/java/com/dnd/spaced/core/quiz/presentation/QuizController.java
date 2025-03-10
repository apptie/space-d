package com.dnd.spaced.core.quiz.presentation;

import com.dnd.spaced.core.quiz.application.QuizService;
import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.AuthAccountInfo;
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
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<Void> createQuiz(
            @AuthAccount AuthAccountInfo accountInfo,
            @RequestBody @Valid CreateQuizRequest request
    ) {
        Long savedQuizId = quizService.createQuiz(accountInfo.id(), request);
        URI location = UriComponentsBuilder.fromPath("/quizzes/{quizId}")
                                           .buildAndExpand(savedQuizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @PostMapping("/{quizId}/graded-answers")
    public ResponseEntity<Void> grade(
            @AuthAccount AuthAccountInfo accountInfo,
            @PathVariable Long quizId,
            @RequestBody @Valid GradeQuizRequest request
    ) {
        quizService.grade(accountInfo.id(), quizId, request);
        URI location = UriComponentsBuilder.fromPath("/quizzes/{id}/graded-answer")
                                           .buildAndExpand(quizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @GetMapping("/graded-answers")
    public ResponseEntity<GradedAnswerCollectionResponse> readGradedAnswers(
            @AuthAccount AuthAccountInfo accountInfo,
            ReadQuizGradedAnswerSearchRequest request,
            @GradedAnswerPageable Pageable pageable
    ) {
        GradedAnswerCollectionResponse response = quizService.readGradedAnswers(accountInfo.id(), request, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{quizId}/graded-answers")
    public ResponseEntity<GradedAnswerCollectionResponse> readGradedAnswers(
            @AuthAccount AuthAccountInfo accountInfo,
            @PathVariable Long quizId
    ) {
        GradedAnswerCollectionResponse response = quizService.readGradedAnswers(quizId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponse> readQuiz(@PathVariable Long quizId) {
        QuizResponse response = quizService.findQuizBy(quizId);

        return ResponseEntity.ok(response);
    }
}
