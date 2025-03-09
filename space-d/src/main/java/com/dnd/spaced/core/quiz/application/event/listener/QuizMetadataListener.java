package com.dnd.spaced.core.quiz.application.event.listener;

import com.dnd.spaced.core.quiz.application.event.dto.AddedQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.event.dto.AddedTodayQuizQuestionEvent;
import com.dnd.spaced.core.quiz.domain.repository.QuizMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuizMetadataListener {

    private final QuizMetadataRepository quizMetadataRepository;

    @EventListener
    @Transactional
    public void listen(AddedQuizQuestionEvent ignored) {
        quizMetadataRepository.updateQuizQuestionCount();
    }

    @EventListener
    @Transactional
    public void listen(AddedTodayQuizQuestionEvent ignored) {
        quizMetadataRepository.updateTodayQuizQuestionCount();
    }
}
