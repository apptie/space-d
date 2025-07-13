package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.quiz.application.event.dto.AddedTodayQuizQuestionEvent;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.dto.mapper.TodayQuizInfoMapper;
import com.dnd.spaced.global.consts.CacheConst;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminTodayQuizServiceFacade {

    private final CreateTodayQuizService createTodayQuizService;
    private final CacheManager memoryCacheManager;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createTodayQuiz() {
        TodayQuiz todayQuiz = createTodayQuizService.assembleTodayQuiz();

        publishAddedTodayQuizQuestionEvent();
        persistMemoryCache(todayQuiz);

        return todayQuiz.getId();
    }

    private void publishAddedTodayQuizQuestionEvent() {
        eventPublisher.publishEvent(new AddedTodayQuizQuestionEvent());
    }

    private void persistMemoryCache(TodayQuiz todayQuiz) {
        Cache cache = memoryCacheManager.getCache(CacheConst.TODAY_QUIZ_CACHE_NAME);

        if (cache != null) {
            cache.clear();
            cache.put(CacheConst.TODAY_QUIZ_CACHE_NAME, TodayQuizInfoMapper.toDto(todayQuiz));
        }
    }
}
