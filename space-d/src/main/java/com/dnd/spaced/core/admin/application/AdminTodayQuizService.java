package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.application.enums.QuizWordCountValidator;
import com.dnd.spaced.core.quiz.application.event.dto.AddedTodayQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.exception.InvalidTodayQuizWordCountException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.dto.mapper.TodayQuizInfoMapper;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizOptionRepository;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.dto.SimpleWordInfo;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.global.config.properties.QuizQuestionProperties;
import com.dnd.spaced.global.consts.CacheConst;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminTodayQuizService {

    private static final Long DEFAULT_WORD_METADATA_ID = 1L;
    private static final int REQUIRED_QUIZ_WORD_COUNT = 4;
    private static final int ANSWER_OPTION_INDEX = 0;

    private final TodayQuizRepository todayQuizRepository;
    private final WordRandomRepository wordRandomRepository;
    private final WordMetadataRepository wordMetadataRepository;
    private final TodayQuizOptionRepository todayQuizOptionRepository;
    private final QuizQuestionProperties quizQuestionProperties;
    private final CacheManager memoryCacheManager;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createTodayQuiz() {
        QuizCategory quizCategory = QuizCategory.findRandom();

        validateQuizCreation(quizCategory);

        TodayQuiz todayQuiz = createTodayQuiz(quizCategory);

        persistMemoryCache(todayQuiz);

        return todayQuiz.getId();
    }

    private void validateQuizCreation(QuizCategory quizCategory) {
        WordMetadata wordMetadata = findWordMetadata();

        validateQuizWordCount(quizCategory, wordMetadata);
    }

    private WordMetadata findWordMetadata() {
        return wordMetadataRepository.findBy(DEFAULT_WORD_METADATA_ID)
                                     .orElseThrow(
                                             () -> new WordMetadataNotFoundException("용어 메타데이터가 정상적으로 설정되지 않았습니다.")
                                     );
    }

    private void validateQuizWordCount(QuizCategory quizCategory, WordMetadata wordMetadata) {
        if (QuizWordCountValidator.isInvalidate(quizCategory, wordMetadata, REQUIRED_QUIZ_WORD_COUNT)) {
            throw new InvalidTodayQuizWordCountException("오늘의 퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
        }
    }

    private TodayQuiz createTodayQuiz(QuizCategory quizCategory) {
        List<SimpleWordInfo> randomWords = findRandomWords(quizCategory);
        TodayQuiz todayQuiz = initTodayQuiz(quizCategory, randomWords);
        TodayQuiz savedTodayQuiz = todayQuizRepository.save(todayQuiz);

        persistTodayQuizOptions(randomWords, todayQuiz);
        publishAddedTodayQuizQuestionEvent();
        return savedTodayQuiz;
    }

    private List<SimpleWordInfo> findRandomWords(QuizCategory quizCategory) {
        return wordRandomRepository.findRandomAllBy(quizCategory, REQUIRED_QUIZ_WORD_COUNT);
    }

    private TodayQuiz initTodayQuiz(QuizCategory quizCategory, List<SimpleWordInfo> randomWords) {
        SimpleWordInfo answerWord = randomWords.get(ANSWER_OPTION_INDEX);
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(
                answerWord.id(),
                answerWord.name()
        );
        TodayQuizQuestion todayQuizQuestion = TodayQuizQuestion.of(
                quizCategory,
                quizQuestionProperties.getQuestion(),
                answerWord.meaning(),
                todayQuizAnswerOption
        );

        return new TodayQuiz(todayQuizQuestion);
    }

    private void persistTodayQuizOptions(List<SimpleWordInfo> randomWords, TodayQuiz todayQuiz) {
        Collections.shuffle(randomWords);

        List<TodayQuizOption> todayQuizOptions = new ArrayList<>();
        for (int i = 0; i < randomWords.size(); i++) {
            SimpleWordInfo word = randomWords.get(i);

            TodayQuizOption todayQuizOption = TodayQuizOption.of(word.id(), word.name(), i, todayQuiz);
            todayQuizOptions.add(todayQuizOption);
        }

        todayQuizOptionRepository.saveAll(todayQuizOptions);
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
