package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import java.util.List;

public interface QuizQuestionRepository {

    List<Long> saveAll(List<QuizQuestion> quizQuestions);
}
