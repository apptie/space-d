package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.mapper.TodayQuizApplicationMapper;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuiz.SubmitAnswer;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizGradedAnswerRepository;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.dnd.spaced.core.skill.application.event.dto.GradedTodayQuizEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayQuizService {

    private static final long TODAY_QUIZ_CORRECT_COUNT = 1L;

    private final ApplicationEventPublisher eventPublisher;
    private final TodayQuizRepository todayQuizRepository;
    private final TodayQuizGradedAnswerRepository todayQuizGradedAnswerRepository;

    public TodayQuizResponse findLatest() {
        TodayQuiz todayQuiz = findLatestQuiz();

        return TodayQuizApplicationMapper.toDto(todayQuiz);
    }

    public void grade(Long accountId, Long todayQuizId, GradeTodayQuizRequest request) {
        TodayQuiz todayQuiz = findTodayQuiz(todayQuizId);
        SubmitAnswer submitAnswer = new SubmitAnswer(request.selectedWordId(), request.selectedContent());
        TodayQuizGradedAnswer gradedAnswer = todayQuiz.grade(accountId, submitAnswer);

        todayQuizGradedAnswerRepository.save(gradedAnswer);
        publishGradedTodayQuizEvent(accountId, gradedAnswer);
    }

    public TodayQuizGradedAnswerCollectionResponse findTodayQuizGradedAnswerAllBy(
            Long accountId,
            ReadTodayQuizGradedAnswerSearchRequest request,
            Pageable pageable
    ) {
        List<TodayQuizGradedAnswer> todayQuizGradedAnswers = todayQuizGradedAnswerRepository.findAllBy(
                accountId,
                request.lastTodayQuizGradedAnswerId(),
                pageable
        );

        return TodayQuizApplicationMapper.toDto(todayQuizGradedAnswers);
    }

    public TodayQuizGradedAnswerResponse findTodayQuizGradedAnswerBy(Long accountId, Long todayQuizId) {
        TodayQuizGradedAnswer todayQuizGradedAnswer = findTodayQuizGradedAnswer(accountId, todayQuizId);

        return TodayQuizApplicationMapper.toDto(todayQuizGradedAnswer);
    }

    public TodayQuizResponse findBy(Long todayQuizId) {
        TodayQuiz todayQuiz = findTodayQuiz(todayQuizId);

        return TodayQuizApplicationMapper.toDto(todayQuiz);
    }

    private TodayQuiz findLatestQuiz() {
        return todayQuizRepository.findLatest()
                                  .orElseThrow(
                                          () -> new TodayQuizNotFoundException("오늘의 퀴즈가 생성되지 않았습니다.")
                                  );
    }

    private TodayQuiz findTodayQuiz(Long todayQuizId) {
        return todayQuizRepository.findBy(todayQuizId)
                                  .orElseThrow(
                                          () -> new TodayQuizNotFoundException(
                                                  "지정한 id의 오늘의 퀴즈를 찾지 못했습니다."
                                          )
                                  );
    }

    private void publishGradedTodayQuizEvent(Long accountId, TodayQuizGradedAnswer gradedAnswer) {
        eventPublisher.publishEvent(new GradedTodayQuizEvent(accountId, calculateCorrectCount(gradedAnswer)));
    }

    private TodayQuizGradedAnswer findTodayQuizGradedAnswer(Long accountId, Long todayQuizId) {
        return todayQuizGradedAnswerRepository.findBy(accountId, todayQuizId)
                                              .orElseThrow(
                                                      () -> new TodayQuizNotFoundException(
                                                              "지정한 id의 오늘의 퀴즈를 찾지 못했습니다."
                                                      )
                                              );
    }

    private long calculateCorrectCount(TodayQuizGradedAnswer answer) {
        if (answer.isCorrect()) {
            return TODAY_QUIZ_CORRECT_COUNT;
        }

        return 0L;
    }
}
