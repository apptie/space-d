package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.QuizMetadata;
import java.util.Optional;

public interface QuizMetadataRepository {

    void save(QuizMetadata quizMetadata);

    void updateQuizQuestionCount();

    void updateTodayQuizQuestionCount();

    Optional<QuizMetadata> findBy(Long quizMetadataId);
}
