package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.exception.AlreadyGradeTodayQuizException;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuiz.SubmitAnswer;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizGradedAnswerRepository;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeTodayQuizService {

    private final TodayQuizRepository todayQuizRepository;
    private final TodayQuizGradedAnswerRepository todayQuizGradedAnswerRepository;

    void gradeTodayQuiz(Long accountId, Long todayQuizId, GradeTodayQuizRequest request) {
        TodayQuiz todayQuiz = findTodayQuiz(todayQuizId);

        validateTodayQuizGradedAnswer(accountId, todayQuizId);
        gradeTodayQuiz(accountId, request, todayQuiz);
    }

    private TodayQuiz findTodayQuiz(Long todayQuizId) {
        return todayQuizRepository.findTodayQuizBy(todayQuizId)
                                  .orElseThrow(
                                          () -> new TodayQuizNotFoundException(
                                                  "지정한 id의 오늘의 퀴즈를 찾지 못했습니다."
                                          )
                                  );
    }

    private void validateTodayQuizGradedAnswer(Long accountId, Long todayQuizId) {
        if (todayQuizGradedAnswerRepository.existsBy(accountId, todayQuizId)) {
            throw new AlreadyGradeTodayQuizException("이미 오늘의 퀴즈를 풀었습니다.");
        }
    }

    private void gradeTodayQuiz(Long accountId, GradeTodayQuizRequest request, TodayQuiz todayQuiz) {
        SubmitAnswer submitAnswer = new SubmitAnswer(request.selectedWordId(), request.selectedContent());
        TodayQuizGradedAnswer gradedAnswer = todayQuiz.grade(accountId, submitAnswer);

        todayQuizGradedAnswerRepository.save(gradedAnswer);
    }
}
