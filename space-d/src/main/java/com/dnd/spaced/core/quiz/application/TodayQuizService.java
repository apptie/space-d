package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.mapper.TodayQuizApplicationMapper;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.exception.AlreadyGradeTodayQuizException;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuiz.SubmitAnswer;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizInfo;
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

    private final ApplicationEventPublisher eventPublisher;
    private final TodayQuizRepository todayQuizRepository;
    private final TodayQuizGradedAnswerRepository todayQuizGradedAnswerRepository;

    public SimpleTodayQuizResponse readLatestTodayQuiz() {
        SimpleTodayQuizInfo simpleTodayQuizInfo = findLatestQuiz();

        return TodayQuizApplicationMapper.toDto(simpleTodayQuizInfo);
    }

    public TodayQuizResponse readTodayQuiz(Long accountId, Long todayQuizId) {
        TodayQuiz todayQuiz = findTodayQuizInfo(todayQuizId);
        boolean solved = todayQuizGradedAnswerRepository.existsBy(accountId, todayQuizId);

        return TodayQuizApplicationMapper.toDto(todayQuiz, accountId, solved);
    }

    @Transactional
    public void grade(Long accountId, Long todayQuizId, GradeTodayQuizRequest request) {
        TodayQuiz todayQuiz = findTodayQuiz(todayQuizId);

        validateTodayQuizGradedAnswer(accountId, todayQuizId);

        SubmitAnswer submitAnswer = new SubmitAnswer(request.selectedWordId(), request.selectedContent());
        TodayQuizGradedAnswer gradedAnswer = todayQuiz.grade(accountId, submitAnswer);

        todayQuizGradedAnswerRepository.save(gradedAnswer);
        publishGradedTodayQuizEvent(accountId, gradedAnswer);
    }

    public TodayQuizGradedAnswerCollectionResponse readTodayQuizGradedAnswers(
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

    public TodayQuizGradedAnswerResponse readTargetTodayQuizGradedAnswers(Long accountId, Long todayQuizId) {
        TodayQuizGradedAnswer todayQuizGradedAnswer = findTodayQuizGradedAnswer(accountId, todayQuizId);

        return TodayQuizApplicationMapper.toDto(todayQuizGradedAnswer);
    }

    private SimpleTodayQuizInfo findLatestQuiz() {
        return todayQuizRepository.findLatest()
                                  .orElseThrow(
                                          () -> new TodayQuizNotFoundException("오늘의 퀴즈가 생성되지 않았습니다.")
                                  );
    }

    private TodayQuiz findTodayQuizInfo(Long todayQuizId) {
        return todayQuizRepository.findWithTodayQuizOptionBy(todayQuizId)
                                  .orElseThrow(
                                          () -> new TodayQuizNotFoundException(
                                                  "지정한 id의 오늘의 퀴즈를 찾지 못했습니다."
                                          )
                                  );
    }

    private TodayQuiz findTodayQuiz(Long todayQuizId) {
        return todayQuizRepository.findTodayQuizBy(todayQuizId)
                                  .orElseThrow(
                                          () -> new TodayQuizNotFoundException(
                                                  "지정한 id의 오늘의 퀴즈를 찾지 못했습니다."
                                          )
                                  );
    }

    private void publishGradedTodayQuizEvent(Long accountId, TodayQuizGradedAnswer gradedAnswer) {
        eventPublisher.publishEvent(new GradedTodayQuizEvent(accountId, gradedAnswer.isCorrect()));
    }

    private TodayQuizGradedAnswer findTodayQuizGradedAnswer(Long accountId, Long todayQuizId) {
        return todayQuizGradedAnswerRepository.findBy(accountId, todayQuizId)
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
}
