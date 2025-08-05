package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.mapper.SimpleTodayQuizResponseMapper;
import com.dnd.spaced.core.quiz.application.dto.mapper.TodayQuizGradedAnswerResponseMapper;
import com.dnd.spaced.core.quiz.application.dto.mapper.TodayQuizGradedAnswerCollectionResponseMapper;
import com.dnd.spaced.core.quiz.application.dto.mapper.TodayQuizResponseMapper;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.ReadTodayQuizDto;
import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import com.dnd.spaced.core.skill.application.event.dto.GradedTodayQuizEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodayQuizServiceFacade {

    private final ReadTodayQuizService readTodayQuizService;
    private final GradeTodayQuizService gradeTodayQuizService;
    private final TodayQuizResponseMapper todayQuizMapper;
    private final SimpleTodayQuizResponseMapper simpleTodayQuizMapper;
    private final TodayQuizGradedAnswerCollectionResponseMapper gradedAnswerMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void gradeTodayQuiz(Long accountId, Long todayQuizId, GradeTodayQuizRequest request) {
        gradeTodayQuizService.gradeTodayQuiz(accountId, todayQuizId, request);
        TodayQuizGradedAnswer todayQuizGradedAnswer = readTodayQuizService.readTargetTodayQuizGradedAnswers(
                accountId,
                todayQuizId
        );

        eventPublisher.publishEvent(GradedTodayQuizEvent.of(accountId, todayQuizGradedAnswer.isCorrect()));
    }

    public SimpleTodayQuizResponse readLatestTodayQuiz() {
        SimpleTodayQuizDto simpleTodayQuizDto = readTodayQuizService.readLatestTodayQuiz();

        return simpleTodayQuizMapper.toResponse(simpleTodayQuizDto);
    }

    public TodayQuizResponse readTodayQuiz(Long accountId, Long todayQuizId) {
        ReadTodayQuizDto readTodayQuizDto = readTodayQuizService.readTodayQuiz(accountId, todayQuizId);

        return todayQuizMapper.toResponse(readTodayQuizDto, accountId);
    }

    public TodayQuizGradedAnswerCollectionResponse readTodayQuizGradedAnswers(
            Long accountId,
            ReadTodayQuizGradedAnswerSearchRequest request,
            Pageable pageable
    ) {
        List<TodayQuizGradedAnswer> todayQuizGradedAnswers = readTodayQuizService.readTodayQuizGradedAnswers(
                accountId,
                request,
                pageable
        );

        return gradedAnswerMapper.toResponse(todayQuizGradedAnswers);
    }

    public TodayQuizGradedAnswerResponse readTargetTodayQuizGradedAnswers(Long accountId, Long todayQuizId) {
        TodayQuizGradedAnswer todayQuizGradedAnswer = readTodayQuizService.readTargetTodayQuizGradedAnswers(
                accountId,
                todayQuizId
        );

        return TodayQuizGradedAnswerResponseMapper.toDto(todayQuizGradedAnswer);
    }
}
