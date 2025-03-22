package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuizGradedAnswerRepository {

    void saveAll(List<QuizGradedAnswer> quizGradedAnswers);

    List<QuizGradedAnswer> findAllBy(Long accountId, Long lastGradedAnswerId, Pageable pageable);

    List<QuizGradedAnswer> findAllBy(Long accountId, Long quizId);
}
