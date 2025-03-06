package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.Quiz;
import java.util.List;
import java.util.Optional;

public interface QuizRepository {

    Quiz save(Quiz quiz);

    List<Quiz> findAllBy(Long accountId);

    Optional<Quiz> findBy(Long id);
}
