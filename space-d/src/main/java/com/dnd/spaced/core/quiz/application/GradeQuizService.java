package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.exception.AlreadyGradeQuizException;
import com.dnd.spaced.core.quiz.application.exception.QuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.Quiz.SubmitAnswer;
import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.QuizGradedAnswerRepository;
import com.dnd.spaced.core.quiz.domain.repository.QuizRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GradeQuizService {

    private final QuizRepository quizRepository;
    private final QuizGradedAnswerRepository quizGradedAnswerRepository;

    public void gradeQuiz(Long accountId, Long quizId, GradeQuizRequest request) {
        Quiz quiz = findQuiz(quizId);

        validateQuiz(quiz);

        convertQuizGradedAnswer(accountId, request, quiz);
    }

    private Quiz findQuiz(Long quizId) {
        return quizRepository.findBy(quizId)
                             .orElseThrow(() -> new QuizNotFoundException("지정한 id의 퀴즈를 찾지 못했습니다."));
    }

    private void validateQuiz(Quiz quiz) {
        if (quiz.isSolved()) {
            throw new AlreadyGradeQuizException("이미 풀었던 퀴즈입니다.");
        }
    }

    private void convertQuizGradedAnswer(Long accountId, GradeQuizRequest request, Quiz quiz) {
        List<SubmitAnswer> submitAnswers = Arrays.stream(request.submitAnswers())
                                                 .map(submitAnswer -> new SubmitAnswer(submitAnswer.wordId(), submitAnswer.content()))
                                                 .toList();
        List<QuizGradedAnswer> quizGradedAnswers = quiz.grade(accountId, submitAnswers);

        quizGradedAnswerRepository.saveAll(quizGradedAnswers);
    }
}
