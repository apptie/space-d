package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.ReadTodayQuizDto;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizGradedAnswerRepository;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReadTodayQuizService {

    private final TodayQuizRepository todayQuizRepository;
    private final TodayQuizGradedAnswerRepository todayQuizGradedAnswerRepository;

    public SimpleTodayQuizDto readLatestTodayQuiz() {
        return todayQuizRepository.findLatest()
                                  .orElseThrow(
                                          () -> new TodayQuizNotFoundException(
                                                  "오늘의 퀴즈가 생성되지 않았습니다.")
                                  );
    }

    public ReadTodayQuizDto readTodayQuiz(Long accountId, Long todayQuizId) {
        TodayQuiz todayQuiz = todayQuizRepository.findWithTodayQuizOptionBy(todayQuizId)
                                                 .orElseThrow(
                                                         () -> new TodayQuizNotFoundException(
                                                                 "지정한 id의 오늘의 퀴즈를 찾지 못했습니다."
                                                         )
                                                 );
        boolean solved = todayQuizGradedAnswerRepository.existsBy(accountId, todayQuizId);

        return new ReadTodayQuizDto(todayQuiz, solved);
    }

    public List<TodayQuizGradedAnswer> readTodayQuizGradedAnswers(
            Long accountId,
            ReadTodayQuizGradedAnswerSearchRequest request,
            Pageable pageable
    ) {
        return todayQuizGradedAnswerRepository.findAllBy(
                accountId,
                request.lastTodayQuizGradedAnswerId(),
                pageable
        );
    }

    public TodayQuizGradedAnswer readTargetTodayQuizGradedAnswers(Long accountId, Long todayQuizId) {
        return todayQuizGradedAnswerRepository.findBy(accountId, todayQuizId)
                                              .orElseThrow(
                                                      () -> new TodayQuizNotFoundException(
                                                              "지정한 오늘의 퀴즈 답안지를 찾지 못했습니다."
                                                      )
                                              );
    }
}
