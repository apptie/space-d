package com.dnd.spaced.core.admin.presentation;

import com.dnd.spaced.core.admin.application.AdminTodayQuizService;
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

    private final AdminTodayQuizService adminTodayQuizService;

    @PostMapping
    public ResponseEntity<Void> createTodayQuiz() {
        Long todayQuizId = adminTodayQuizService.create();
        URI location = UriComponentsBuilder.fromPath("/today-quizzes/{todayQuizId}")
                                           .buildAndExpand(todayQuizId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }
}
