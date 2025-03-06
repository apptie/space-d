package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.mapper.TodayQuizApplicationMapper;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizGradedAnswerRepository;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayQuizService {

    private final TodayQuizRepository todayQuizRepository;
    private final TodayQuizGradedAnswerRepository todayQuizGradedAnswerRepository;

    public TodayQuizResponse findLatest() {
        TodayQuiz todayQuiz = todayQuizRepository.findLatest()
                                                 .orElseThrow(
                                                         () -> new TodayQuizNotFoundException("오늘의 퀴즈가 생성되지 않았습니다."));

        return TodayQuizApplicationMapper.toDto(todayQuiz);
    }

    public void grade(Long accountId, Long todayQuizId, GradeTodayQuizRequest request) {
        TodayQuiz todayQuiz = todayQuizRepository.findBy(todayQuizId)
                                                 .orElseThrow(
                                                         () -> new TodayQuizNotFoundException(
                                                                 "지정한 id의 오늘의 퀴즈를 찾지 못했습니다."
                                                         )
                                                 );
        TodayQuizGradedAnswer gradedAnswer = todayQuiz.grade(accountId, request.answer());

        todayQuizGradedAnswerRepository.save(gradedAnswer);
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
        List<TodayQuizGradedAnswerResponse> responses = todayQuizGradedAnswers.stream()
                                                                              .map(TodayQuizApplicationMapper::toDto)
                                                                              .toList();

        return new TodayQuizGradedAnswerCollectionResponse(responses);
    }

    public TodayQuizGradedAnswerResponse findTodayQuizGradedAnswerBy(Long accountId, Long todayQuizId) {
        TodayQuizGradedAnswer todayQuizGradedAnswer = todayQuizGradedAnswerRepository.findBy(accountId, todayQuizId)
                                                                                     .orElseThrow(
                                                                                             () -> new TodayQuizNotFoundException(
                                                                                                     "지정한 id의 오늘의 퀴즈를 찾지 못했습니다."
                                                                                             )
                                                                                     );

        return TodayQuizApplicationMapper.toDto(todayQuizGradedAnswer);
    }

    public TodayQuizResponse findBy(Long todayQuizId) {
        TodayQuiz todayQuiz = todayQuizRepository.findBy(todayQuizId)
                                                 .orElseThrow(() -> new TodayQuizNotFoundException("지정한 id의 오늘의 퀴즈를 찾지 못했습니다."));

        return TodayQuizApplicationMapper.toDto(todayQuiz);
    }
}
