package com.dnd.spaced.core.quiz.infrastructure.persistence;

import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.repository.QuizOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizOptionGatewayRepository implements QuizOptionRepository {

    private final QuizOptionCrudRepository quizOptionCrudRepository;

    @Override
    public QuizOption save(QuizOption quizOption) {
        return quizOptionCrudRepository.save(quizOption);
    }
}
