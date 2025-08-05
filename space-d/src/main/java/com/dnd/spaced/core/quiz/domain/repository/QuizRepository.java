package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto;
import com.dnd.spaced.core.quiz.domain.dto.SimpleQuizDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface QuizRepository {

    Quiz save(Quiz quiz);

    List<Quiz> findAllBy(Long accountId);

    Optional<Quiz> findBy(Long quizId);

    Optional<QuizDto> findBy(Long quizId, Long accountId);

    List<SimpleQuizDto> findAllBy(Long accountId, Long lastQuizId, Pageable pageable);
}
