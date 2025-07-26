package com.dnd.spaced.config;

import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import com.dnd.spaced.core.skill.application.event.dto.FailedGradedQuizSkillEvent;
import com.dnd.spaced.core.skill.application.event.dto.FailedGradedTodayQuizSkillEvent;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import com.dnd.spaced.core.word.application.repository.DeletedWordIdRepository;
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
    SkillRepository skillRepository;

    @SpyBean
    RedisTemplate<String, FailedGradedQuizSkillEvent> gradedQuizEventFailedRedisTemplate;

    @SpyBean
    RedisTemplate<String, FailedGradedTodayQuizSkillEvent> gradedTodayQuizEventFailedRedisTemplate;

    @SpyBean
    WordRepository wordRepository;

    @SpyBean
    WordExampleRepository wordExampleRepository;

    @SpyBean
    PronunciationRepository pronunciationRepository;

    @SpyBean
    WordRandomRepository wordRandomRepository;

    @SpyBean
    WordMetadataRepository wordMetadataRepository;

    @SpyBean
    RedisTemplate<String, FailedWordPersistedEvent> wordPersistFailedRedisTemplate;

    @SpyBean
    DeletedWordIdRepository deletedWordIdRepository;

    @SpyBean
    BookmarkRepository bookmarkRepository;
}
