package com.dnd.spaced.core.quiz.domain.repository;

import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GradedAnswerRepository {

    void saveAll(List<GradedAnswer> gradedAnswers);

    List<GradedAnswer> findAllBy(Long accountId, Long lastGradedAnswerId, Pageable pageable);

    List<GradedAnswer> findAllBy(Long quizId);
}
