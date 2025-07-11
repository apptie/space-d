package com.dnd.spaced.core.admin.presentation;

import com.dnd.spaced.core.admin.application.AdminTodayQuizServiceFacade;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/admin/today-quizzes")
@RequiredArgsConstructor
public class AdminTodayQuizController {

    private final AdminTodayQuizServiceFacade adminTodayQuizServiceFacade;

    @PostMapping
    public ResponseEntity<Void> createTodayQuiz() {
        Long todayQuizId = adminTodayQuizServiceFacade.createTodayQuiz();
        URI location = UriComponentsBuilder.fromPath("/today-quizzes/{todayQuizId}")
                                           .buildAndExpand(todayQuizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }
}
