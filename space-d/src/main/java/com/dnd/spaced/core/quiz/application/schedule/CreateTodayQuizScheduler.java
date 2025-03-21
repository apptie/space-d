package com.dnd.spaced.core.quiz.application.schedule;

import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.application.enums.QuizWordCountValidator;
import com.dnd.spaced.core.quiz.application.exception.InvalidTodayQuizWordCountException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.dto.mapper.TodayQuizInfoMapper;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizOptionRepository;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.WordRandom;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.global.config.properties.QuizQuestionProperties;
import com.dnd.spaced.global.consts.CacheConst;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateTodayQuizScheduler {

    private static final Long DEFAULT_WORD_METADATA_ID = 1L;
    private static final int REQUIRED_QUIZ_WORD_COUNT = 4;
    private static final int ANSWER_OPTION_INDEX = 0;

    private final TodayQuizRepository todayQuizRepository;
    private final WordRandomRepository wordRandomRepository;
    private final WordMetadataRepository wordMetadataRepository;
    private final TodayQuizOptionRepository todayQuizOptionRepository;
    private final QuizQuestionProperties quizQuestionProperties;
    private final CacheManager memoryCacheManager;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void schedule() {
        QuizCategory quizCategory = QuizCategory.findRandom();

        validateQuizCreation(quizCategory);

        List<Word> randomWords = findRandomWords(quizCategory);
        TodayQuiz todayQuiz = createTodayQuiz(randomWords, quizCategory);
        TodayQuiz savedTodayQuiz = todayQuizRepository.save(todayQuiz);

        initTodayQuizOption(randomWords, todayQuiz);
        persistMemoryCache(savedTodayQuiz);
    }

    private TodayQuiz createTodayQuiz(List<Word> randomWords, QuizCategory quizCategory) {
        Word answerWord = randomWords.get(ANSWER_OPTION_INDEX);
        TodayQuizAnswerOption todayQuizAnswerOption = new TodayQuizAnswerOption(
                answerWord.getId(),
                answerWord.getName()
        );
        TodayQuizQuestion todayQuizQuestion = TodayQuizQuestion.of(
                quizCategory,
                quizQuestionProperties.getQuestion(),
                answerWord.getWordMeaning().getMeaning(),
                todayQuizAnswerOption
        );

        return new TodayQuiz(todayQuizQuestion);
    }

    private void initTodayQuizOption(List<Word> randomWords, TodayQuiz todayQuiz) {
        Collections.shuffle(randomWords);
        for (int i = 0; i < randomWords.size(); i++) {
            Word word = randomWords.get(i);

            TodayQuizOption todayQuizOption = TodayQuizOption.of(word.getId(), word.getName(), i, todayQuiz);
            todayQuizOptionRepository.save(todayQuizOption);
        }
    }

    private void validateQuizCreation(QuizCategory quizCategory) {
        WordMetadata wordMetadata = wordMetadataRepository.findBy(DEFAULT_WORD_METADATA_ID)
                                                          .orElseThrow(() -> new WordMetadataNotFoundException(
                                                                  "용어 메타데이터가 정상적으로 설정되지 않았습니다.")
                                                          );

        if (QuizWordCountValidator.isInvalidate(quizCategory, wordMetadata, REQUIRED_QUIZ_WORD_COUNT)) {
            throw new InvalidTodayQuizWordCountException("오늘의 퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
        }
    }

    private List<Word> findRandomWords(QuizCategory quizCategory) {
        return wordRandomRepository.findRandomAllBy(quizCategory, REQUIRED_QUIZ_WORD_COUNT)
                                   .stream()
                                   .map(WordRandom::getWord)
                                   .toList();
    }

    private void persistMemoryCache(TodayQuiz todayQuiz) {
        Cache cache = memoryCacheManager.getCache(CacheConst.TODAY_QUIZ_CACHE_NAME);

        if (cache != null) {
            cache.clear();
            cache.put(CacheConst.TODAY_QUIZ_CACHE_NAME, TodayQuizInfoMapper.toDto(todayQuiz));
        }
    }
}
