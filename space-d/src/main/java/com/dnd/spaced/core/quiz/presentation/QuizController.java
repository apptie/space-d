package com.dnd.spaced.core.quiz.presentation;

import com.dnd.spaced.core.quiz.application.QuizServiceFacade;
import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadAllQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.global.auth.resolver.AuthAccountId;
import com.dnd.spaced.global.auth.resolver.CurrentAccount;
import com.dnd.spaced.global.resolver.quiz.GradedAnswerPageable;
import com.dnd.spaced.global.resolver.quiz.QuizPageable;
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

    private final QuizServiceFacade quizServiceFacade;

    @PostMapping
    public ResponseEntity<Void> createQuiz(
            @CurrentAccount AuthAccountId accountId,
            @Valid @RequestBody CreateQuizRequest request
    ) {
        Long savedQuizId = quizServiceFacade.createQuiz(accountId.id(), request);
        URI location = UriComponentsBuilder.fromPath("/quizzes/{quizId}")
                                           .buildAndExpand(savedQuizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @PostMapping("/{quizId}/graded-answers")
    public ResponseEntity<Void> gradeQuiz(
            @CurrentAccount AuthAccountId accountId,
            @PathVariable Long quizId,
            @Valid @RequestBody GradeQuizRequest request
    ) {
        quizServiceFacade.grade(accountId.id(), quizId, request);
        URI location = UriComponentsBuilder.fromPath("/quizzes/{id}/graded-answer")
                                           .buildAndExpand(quizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @GetMapping("/graded-answers")
    public ResponseEntity<QuizGradedAnswerCollectionResponse> readQuizGradedAnswers(
            @CurrentAccount AuthAccountId accountId,
            ReadQuizGradedAnswerSearchRequest request,
            @GradedAnswerPageable Pageable pageable
    ) {
        QuizGradedAnswerCollectionResponse response = quizServiceFacade.readGradedAnswers(
                accountId.id(),
                request,
                pageable
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{quizId}/graded-answers")
    public ResponseEntity<QuizGradedAnswerCollectionResponse> readTargetQuizGradedAnswers(
            @CurrentAccount AuthAccountId accountId,
            @PathVariable Long quizId
    ) {
        QuizGradedAnswerCollectionResponse response = quizServiceFacade.readGradedAnswers(accountId.id(), quizId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponse> readQuiz(
            @CurrentAccount AuthAccountId accountId,
            @PathVariable Long quizId
    ) {
        QuizResponse response = quizServiceFacade.readQuiz(accountId.id(), quizId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<QuizCollectionResponse> readQuizzes(
            @CurrentAccount AuthAccountId accountId,
            ReadAllQuizRequest request,
            @QuizPageable Pageable pageable
    ) {
        QuizCollectionResponse response = quizServiceFacade.readQuizzes(accountId.id(), request, pageable);

        return ResponseEntity.ok(response);
    }
}
