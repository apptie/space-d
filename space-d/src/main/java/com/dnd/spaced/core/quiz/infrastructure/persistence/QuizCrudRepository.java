package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

interface QuizCrudRepository extends JpaRepository<Quiz, Long> {
}
