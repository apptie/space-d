package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.QuizOption;
import java.util.List;

public interface QuizOptionRepository {

    QuizOption save(QuizOption quizOption);

    void saveAll(List<QuizOption> quizOptions);
}
