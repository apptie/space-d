package com.dnd.spaced.config;

import com.dnd.spaced.core.skill.application.event.dto.FailedGradedQuizSkillEvent;
import com.dnd.spaced.core.skill.application.event.dto.FailedGradedTodayQuizSkillEvent;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import com.dnd.spaced.core.word.application.event.dto.FailedWordPersistedEvent;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

@Profile("test")
@Configuration
public class EventListenerSpyBeanTestConfig {

    @SpyBean
    public SkillRepository skillRepository;

    @SpyBean
    public RedisTemplate<String, FailedGradedQuizSkillEvent> gradedQuizEventFailedRedisTemplate;

    @SpyBean
    public RedisTemplate<String, FailedGradedTodayQuizSkillEvent> gradedTodayQuizEventFailedRedisTemplate;

    @SpyBean
    public WordRepository wordRepository;

    @SpyBean
    public WordExampleRepository wordExampleRepository;

    @SpyBean
    public PronunciationRepository pronunciationRepository;

    @SpyBean
    public WordRandomRepository wordRandomRepository;

    @SpyBean
    public WordMetadataRepository wordMetadataRepository;

    @SpyBean
    public RedisTemplate<String, FailedWordPersistedEvent> wordPersistFailedRedisTemplate;
}
