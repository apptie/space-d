package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.application.exception.InvalidTodayQuizWordCountException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizOptionRepository;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.dnd.spaced.core.quiz.domain.service.QuizWordCountValidator;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.dto.SimpleWord;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.global.config.properties.QuizQuestionProperties;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTodayQuizService {

    private static final Long DEFAULT_WORD_METADATA_ID = 1L;
    private static final int REQUIRED_TODAY_QUIZ_WORD_COUNT = 4;
    private static final int ANSWER_OPTION_INDEX = 0;

    private final TodayQuizRepository todayQuizRepository;
    private final WordRandomRepository wordRandomRepository;
    private final WordMetadataRepository wordMetadataRepository;
    private final TodayQuizOptionRepository todayQuizOptionRepository;
    private final QuizQuestionProperties quizQuestionProperties;

    public TodayQuiz createTodayQuiz() {
        QuizCategory quizCategory = findRandomQuizCategory();

        validateQuizCreationRequirements(quizCategory);

        return createTodayQuiz(quizCategory);
    }

    private QuizCategory findRandomQuizCategory() {
        return QuizCategory.findRandom();
    }

    private void validateQuizCreationRequirements(QuizCategory quizCategory) {
        WordMetadata wordMetadata = findWordMetadata();

        validateWordCount(quizCategory, wordMetadata);
    }

    private WordMetadata findWordMetadata() {
        return wordMetadataRepository.findBy(DEFAULT_WORD_METADATA_ID)
                                     .orElseThrow(
                                             () -> new WordMetadataNotFoundException("용어 메타데이터가 정상적으로 설정되지 않았습니다.")
                                     );
    }

    private void validateWordCount(QuizCategory quizCategory, WordMetadata wordMetadata) {
        QuizWordCountValidator validator = QuizWordCountValidator.create();

        if (validator.isInvalidate(quizCategory, wordMetadata, REQUIRED_TODAY_QUIZ_WORD_COUNT)) {
            throw new InvalidTodayQuizWordCountException("오늘의 퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
        }
    }

    private TodayQuiz createTodayQuiz(QuizCategory quizCategory) {
        List<SimpleWord> randomWords = findRandomWords(quizCategory);
        TodayQuiz todayQuiz = initTodayQuiz(quizCategory, randomWords);
        TodayQuiz persistedTodayQuiz = persistTodayQuiz(todayQuiz);

        persistTodayQuizOptions(randomWords, todayQuiz);
        return persistedTodayQuiz;
    }

    private List<SimpleWord> findRandomWords(QuizCategory quizCategory) {
        return wordRandomRepository.findRandomAllBy(quizCategory, REQUIRED_TODAY_QUIZ_WORD_COUNT);
    }

    private TodayQuiz initTodayQuiz(QuizCategory quizCategory, List<SimpleWord> randomWords) {
        SimpleWord answerWord = randomWords.get(ANSWER_OPTION_INDEX);
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

    private TodayQuiz persistTodayQuiz(TodayQuiz todayQuiz) {
        return todayQuizRepository.save(todayQuiz);
    }

    private void persistTodayQuizOptions(List<SimpleWord> randomWords, TodayQuiz todayQuiz) {
        List<SimpleWord> shuffledWords = shuffleRandomWords(randomWords);
        List<TodayQuizOption> todayQuizOptions = initTodayQuizOptions(shuffledWords, todayQuiz);

        saveAllTodayQuizOptions(todayQuizOptions);
    }

    private List<SimpleWord> shuffleRandomWords(List<SimpleWord> randomWords) {
        Collections.shuffle(randomWords);

        return randomWords;
    }

    private List<TodayQuizOption> initTodayQuizOptions(List<SimpleWord> randomWords, TodayQuiz todayQuiz) {
        return IntStream.range(0, randomWords.size())
                        .mapToObj(i -> {
                            SimpleWord simpleWord = randomWords.get(i);

                            return TodayQuizOption.of(simpleWord.id(), simpleWord.name(), i, todayQuiz);
                        })
                        .toList();
    }

    private void saveAllTodayQuizOptions(List<TodayQuizOption> todayQuizOptions) {
        todayQuizOptionRepository.saveAll(todayQuizOptions);
    }
}
