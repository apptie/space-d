package com.dnd.spaced.core.skill.application.event.listener;

import com.dnd.spaced.core.skill.application.event.dto.FailedGradedQuizSkillEvent;
import com.dnd.spaced.core.skill.application.event.dto.FailedGradedTodayQuizSkillEvent;
import com.dnd.spaced.core.skill.application.event.dto.GradedQuizEvent;
import com.dnd.spaced.core.skill.application.event.dto.GradedTodayQuizEvent;
import com.dnd.spaced.core.skill.application.event.listener.exception.SkillNotFoundException;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import com.dnd.spaced.global.consts.LogConst;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class GradedQuizEventListener {

    private static final String QUIZ_KEY = "failed-graded-today-quiz";
    private static final String TODAY_QUIZ_KEY = "failed-graded-today-quiz";

    private final Clock clock;
    private final SkillRepository skillRepository;
    private final RetryTemplate gradedQuizRetryTemplate;
    private final RedisTemplate<String, FailedGradedQuizSkillEvent> gradedQuizEventFailedRedisTemplate;
    private final RedisTemplate<String, FailedGradedTodayQuizSkillEvent> gradedTodayQuizEventFailedRedisTemplate;

    @Async("asyncCalculateSkillExecutor")
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void listen(GradedQuizEvent event) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        try {
            gradedQuizRetryTemplate.execute(
                    retryContext -> {
                        Skill skill = findSkill(event.accountId());

                        skill.addCorrectQuizQuestion(event.correctCount());

                        return null;
                    }, retryContext -> {
                        log.error(
                                "[{}] {} 사용자 퀴즈 풀이 이후 이벤트 처리 실패",
                                requestId,
                                event.accountId(),
                                retryContext.getLastThrowable()
                        );
                        recoverCalculateQuizSkill(event);

                        return null;
                    }
            );
        } catch (Throwable e) {
            log.error("[{}] retry 실패", requestId, e);
        }
    }

    @Async("asyncCalculateSkillExecutor")
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void listen(GradedTodayQuizEvent event) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        try {
            gradedQuizRetryTemplate.execute(
                    retryContext -> {
                        Skill skill = findSkill(event.accountId());

                        skill.addCorrectTodayQuizQuestion(event.correctCount());

                        return null;
                    }, retryContext -> {
                        log.error(
                                "[{}] {} 사용자 오늘의 퀴즈 풀이 이후 이벤트 처리 실패",
                                requestId,
                                event.accountId(),
                                retryContext.getLastThrowable()
                        );
                        recoverCalculateTodayQuizSkill(event);

                        return null;
                    }
            );
        } catch (Throwable e) {
            log.error("[{}] retry 실패", requestId, e);
        }
    }

    private Skill findSkill(Long accountId) {
        return skillRepository.findBy(accountId)
                              .orElseThrow(() -> new SkillNotFoundException("지정한 회원의 스킬을 찾지 못했습니다."));
    }

    private void recoverCalculateQuizSkill(GradedQuizEvent event) {
        FailedGradedQuizSkillEvent failedEvent = new FailedGradedQuizSkillEvent(
                event.accountId(),
                event.correctCount(),
                LocalDateTime.now(clock)
        );

        gradedQuizEventFailedRedisTemplate.opsForList()
                                          .rightPush(QUIZ_KEY, failedEvent);
    }

    private void recoverCalculateTodayQuizSkill(GradedTodayQuizEvent event) {
        FailedGradedTodayQuizSkillEvent failedEvent = new FailedGradedTodayQuizSkillEvent(
                event.accountId(),
                LocalDateTime.now(clock)
        );

        gradedTodayQuizEventFailedRedisTemplate.opsForList()
                                               .rightPush(TODAY_QUIZ_KEY, failedEvent);
    }
}
