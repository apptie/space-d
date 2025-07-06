package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.mapper.QuizGradedAnswerCollectionResponseMapper;
import com.dnd.spaced.core.quiz.application.dto.mapper.QuizResponseMapper;
import com.dnd.spaced.core.quiz.application.dto.mapper.QuizCollectionResponseMapper;
import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadAllQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.event.dto.AddedQuizQuestionEvent;
import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto;
import com.dnd.spaced.core.quiz.domain.dto.SimpleQuizDto;
import com.dnd.spaced.core.skill.application.event.dto.GradedQuizEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizServiceFacade {

    private final CreateQuizService createQuizService;
    private final GradeQuizService gradeQuizService;
    private final ReadQuizService readQuizService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createQuiz(Long accountId, CreateQuizRequest request) {
        Long quizId = createQuizService.createQuiz(accountId, request);

        eventPublisher.publishEvent(new AddedQuizQuestionEvent());
        return quizId;
    }

    @Transactional
    public void grade(Long accountId, Long quizId, GradeQuizRequest request) {
        gradeQuizService.gradeQuiz(accountId, quizId, request);

        List<QuizGradedAnswer> quizGradedAnswers = readQuizService.readGradedAnswers(accountId, quizId);

        eventPublisher.publishEvent(GradedQuizEvent.of(accountId, quizGradedAnswers));
    }

    public QuizGradedAnswerCollectionResponse readGradedAnswers(
            Long accountId,
            ReadQuizGradedAnswerSearchRequest request,
            Pageable pageable
    ) {
        List<QuizGradedAnswer> quizGradedAnswers = readQuizService.readGradedAnswers(accountId, request, pageable);

        return QuizGradedAnswerCollectionResponseMapper.toCollectionDto(quizGradedAnswers);
    }

    public QuizGradedAnswerCollectionResponse readGradedAnswers(Long accountId, Long quizId) {
        List<QuizGradedAnswer> quizGradedAnswers = readQuizService.readGradedAnswers(accountId, quizId);

        return QuizGradedAnswerCollectionResponseMapper.toCollectionDto(quizGradedAnswers);
    }

    public QuizResponse readQuiz(Long accountId, Long quizId) {
        QuizDto quizDto = readQuizService.readQuiz(quizId, accountId);

        return QuizResponseMapper.toDto(quizDto);
    }

    public QuizCollectionResponse readQuizzes(Long accountId, ReadAllQuizRequest request, Pageable pageable) {
        List<SimpleQuizDto> quizzes = readQuizService.readQuizzes(accountId, request, pageable);

        return QuizCollectionResponseMapper.toCollectionResponse(quizzes);
    }
}
