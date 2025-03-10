package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.repository.QuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizQuestionGatewayRepository implements QuizQuestionRepository {

    private final QuizQuestionCrudRepository quizQuestionCrudRepository;

    @Override
    public QuizQuestion save(QuizQuestion quizQuestion) {
        return quizQuestionCrudRepository.save(quizQuestion);
    }
}
