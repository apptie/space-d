package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.exception.InvalidQuizWordCountException;
import com.dnd.spaced.core.quiz.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.QuizOptionRepository;
import com.dnd.spaced.core.quiz.domain.repository.QuizQuestionRepository;
import com.dnd.spaced.core.quiz.domain.repository.QuizRepository;
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
class CreateQuizService {

    private static final Long DEFAULT_WORD_METADATA_ID = 1L;
    private static final int QUIZ_QUESTION_WORD_COUNT = 5;
    private static final int REQUIRED_QUIZ_WORD_COUNT = 20;
    private static final int REQUIRED_QUESTION_WORD_COUNT = 4;
    private static final int ANSWER_OPTION_INDEX = 0;

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final WordRandomRepository wordRandomRepository;
    private final WordMetadataRepository wordMetadataRepository;
    private final QuizQuestionProperties quizQuestionProperties;

    public Long createQuiz(Long accountId, CreateQuizRequest request) {
        QuizCategory quizCategory = findQuizCategory(request);

        validateQuizCreation(quizCategory);

        Quiz quiz = createQuiz(accountId, quizCategory);

        return quiz.getId();
    }

    private QuizCategory findQuizCategory(CreateQuizRequest request) {
        return QuizCategory.findBy(request.quizCategoryName());
    }

    private void validateQuizCreation(QuizCategory quizCategory) {
        WordMetadata wordMetadata = findWordMetadata();

        validateQuizMetadata(quizCategory, wordMetadata);
    }

    private WordMetadata findWordMetadata() {
        return wordMetadataRepository.findBy(DEFAULT_WORD_METADATA_ID)
                                     .orElseThrow(() -> new WordMetadataNotFoundException(
                                                                  "용어 메타데이터가 정상적으로 설정되지 않았습니다.")
                                                          );
    }

    private void validateQuizMetadata(QuizCategory quizCategory, WordMetadata wordMetadata) {
        QuizWordCountValidator quizWordCountValidator = QuizWordCountValidator.create();

        if (quizWordCountValidator.isInvalidate(quizCategory, wordMetadata, REQUIRED_QUIZ_WORD_COUNT)) {
            throw new InvalidQuizWordCountException("퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
        }
    }

    private Quiz createQuiz(Long accountId, QuizCategory quizCategory) {
        Quiz quiz = persistQuiz(accountId);
        List<List<SimpleWord>> randomSplitWords = findRandomSplitWords(quizCategory);
        List<Long> quizQuestionIds = persistQuizQuestion(quizCategory, randomSplitWords, quiz);

        persistQuizOptions(randomSplitWords, quizQuestionIds);
        return quiz;
    }

    private Quiz persistQuiz(Long accountId) {
        Quiz quiz = new Quiz(accountId);

        return quizRepository.save(quiz);
    }

    private List<List<SimpleWord>> findRandomSplitWords(QuizCategory quizCategory) {
        List<SimpleWord> randomWords = findRandomWords(quizCategory);

        return splitByQuestionWordCount(randomWords);
    }

    private List<SimpleWord> findRandomWords(QuizCategory quizCategory) {
        return wordRandomRepository.findRandomAllBy(quizCategory, REQUIRED_QUIZ_WORD_COUNT);
    }

    private List<List<SimpleWord>> splitByQuestionWordCount(List<SimpleWord> words) {
        return IntStream.range(0, QUIZ_QUESTION_WORD_COUNT)
                        .mapToObj(index -> index * REQUIRED_QUESTION_WORD_COUNT)
                        .map(index -> splitByIndex(words, index))
                        .toList();
    }

    private List<SimpleWord> splitByIndex(List<SimpleWord> words, Integer startIndex) {
        int endIndex = Math.min(startIndex + REQUIRED_QUESTION_WORD_COUNT, REQUIRED_QUIZ_WORD_COUNT);

        return words.subList(startIndex, endIndex);
    }

    private List<Long> persistQuizQuestion(
            QuizCategory quizCategory,
            List<List<SimpleWord>> splitWords,
            Quiz savedQuiz
    ) {
        List<QuizQuestion> quizQuestions = splitWords.stream()
                                                     .map(words -> convertQuizQuestion(quizCategory, savedQuiz, words))
                                                     .toList();

        return quizQuestionRepository.saveAll(quizQuestions);
    }

    private QuizQuestion convertQuizQuestion(QuizCategory quizCategory, Quiz savedQuiz, List<SimpleWord> words) {
        SimpleWord answerWord = words.get(ANSWER_OPTION_INDEX);

        return QuizQuestion.of(
                quizCategory,
                quizQuestionProperties.getQuestion(),
                answerWord.meaning(),
                new QuizAnswerOption(
                        answerWord.id(),
                        answerWord.name()
                ),
                savedQuiz
        );
    }

    private void persistQuizOptions(List<List<SimpleWord>> splitWords, List<Long> quizQuestionIds) {
        List<QuizOption> quizOptions = IntStream.range(0, splitWords.size())
                                                .mapToObj(index ->
                                                        convertQuizOptions(
                                                                splitWords.get(index),
                                                                quizQuestionIds.get(index)
                                                        )
                                                )
                                                .flatMap(List::stream)
                                                .toList();

        quizOptionRepository.saveAll(quizOptions);
    }

    private List<QuizOption> convertQuizOptions(List<SimpleWord> targetWords, Long targetQuizQuestionId) {
        Collections.shuffle(targetWords);

        return IntStream.range(0, targetWords.size())
                        .mapToObj(index -> convertQuizOption(targetQuizQuestionId, index, targetWords.get(index)))
                        .toList();
    }

    private QuizOption convertQuizOption(Long targetQuizQuestionId, int index, SimpleWord word) {
        return QuizOption.of(word.id(), word.name(), index, targetQuizQuestionId);
    }
}
