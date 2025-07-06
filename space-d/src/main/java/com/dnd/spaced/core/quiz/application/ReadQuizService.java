package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.request.ReadAllQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.exception.QuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto;
import com.dnd.spaced.core.quiz.domain.dto.SimpleQuizDto;
import com.dnd.spaced.core.quiz.domain.repository.QuizGradedAnswerRepository;
import com.dnd.spaced.core.quiz.domain.repository.QuizRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadQuizService {

    private final QuizRepository quizRepository;
    private final QuizGradedAnswerRepository quizGradedAnswerRepository;

    List<QuizGradedAnswer> readGradedAnswers(
            Long accountId,
            ReadQuizGradedAnswerSearchRequest request,
            Pageable pageable
    ) {
        return quizGradedAnswerRepository.findAllBy(
                accountId,
                request.lastQuizGradedAnswerId(),
                pageable
        );
    }

    List<QuizGradedAnswer>readGradedAnswers(Long accountId, Long quizId) {
        return quizGradedAnswerRepository.findAllBy(accountId, quizId);
    }

    QuizDto readQuiz(Long accountId, Long quizId) {
        return quizRepository.findBy(quizId, accountId)
                                        .orElseThrow(
                                                () -> new QuizNotFoundException("지정한 id의 퀴즈를 찾지 못했습니다.")
                                        );
    }

    List<SimpleQuizDto> readQuizzes(Long accountId, ReadAllQuizRequest request, Pageable pageable) {
        return quizRepository.findAllBy(accountId, request.lastQuizId(), pageable);
    }
}
