package com.dnd.spaced.core.quiz.infrastructure.repository;

import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface QuizQuestionCrudRepository extends CrudRepository<QuizQuestion, Long> {

    List<QuizQuestion> findAllByQuizId(Long quizId);
}
