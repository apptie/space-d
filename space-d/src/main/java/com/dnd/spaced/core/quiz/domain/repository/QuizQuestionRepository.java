package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.QuizQuestion;

public interface QuizQuestionRepository {

    QuizQuestion save(QuizQuestion quizQuestion);
}
