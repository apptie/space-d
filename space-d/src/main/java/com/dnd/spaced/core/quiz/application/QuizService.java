package com.dnd.spaced.core.quiz.application;

import com.dnd.spaced.core.quiz.application.dto.mapper.QuizApplicationMapper;
import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.enums.QuizWordCountValidator;
import com.dnd.spaced.core.quiz.application.event.dto.AddedQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.exception.InvalidQuizWordCountException;
import com.dnd.spaced.core.quiz.application.exception.QuizNotFoundException;
import com.dnd.spaced.core.quiz.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import com.dnd.spaced.core.quiz.domain.Quiz;
import com.dnd.spaced.core.quiz.domain.QuizOption;
import com.dnd.spaced.core.quiz.domain.QuizQuestion;
import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.GradedAnswerRepository;
import com.dnd.spaced.core.quiz.domain.repository.QuizOptionRepository;
import com.dnd.spaced.core.quiz.domain.repository.QuizQuestionRepository;
import com.dnd.spaced.core.quiz.domain.repository.QuizRepository;
import com.dnd.spaced.core.skill.application.event.dto.GradedQuizEvent;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.WordRandom;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.global.config.properties.QuizQuestionProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {

    private static final Long DEFAULT_WORD_METADATA_ID = 1L;
    private static final int REQUIRED_QUIZ_WORD_COUNT = 20;
    private static final int REQUIRED_QUESTION_WORD_COUNT = 4;
    private static final int ANSWER_OPTION_INDEX = 0;

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final WordRepository wordRepository;
    private final WordRandomRepository wordRandomRepository;
    private final WordMetadataRepository wordMetadataRepository;
    private final GradedAnswerRepository gradedAnswerRepository;
    private final QuizQuestionProperties quizQuestionProperties;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createQuiz(Long accountId, CreateQuizRequest request) {
        QuizCategory quizCategory = QuizCategory.findBy(request.quizCategoryName());

        validateQuizCreation(quizCategory);

        Quiz quiz = createQuiz(accountId, quizCategory);
        Quiz savedQuiz = quizRepository.save(quiz);

        publishAddedQuizQuestionEvent();
        return savedQuiz.getId();
    }

    @Transactional
    public void grade(Long accountId, Long quizId, GradeQuizRequest request) {
        Quiz quiz = findQuiz(quizId);
        List<GradedAnswer> gradedAnswers = quiz.grade(accountId, request.answers());

        gradedAnswerRepository.saveAll(gradedAnswers);
        long correctCount = gradedAnswers.stream()
                                         .filter(GradedAnswer::isCorrect)
                                         .count();
        eventPublisher.publishEvent(new GradedQuizEvent(accountId, correctCount));
    }

    public GradedAnswerCollectionResponse readGradedAnswers(
            Long accountId,
            ReadQuizGradedAnswerSearchRequest request,
            Pageable pageable
    ) {
        List<GradedAnswer> gradedAnswers = gradedAnswerRepository.findAllBy(
                accountId,
                request.lastQuizGradedAnswerId(),
                pageable
        );

        return QuizApplicationMapper.toDto(gradedAnswers);
    }

    public GradedAnswerCollectionResponse readGradedAnswers(Long quizId) {
        List<GradedAnswer> gradedAnswers = gradedAnswerRepository.findAllBy(quizId);

        return QuizApplicationMapper.toDto(gradedAnswers);
    }

    public QuizResponse findQuizBy(Long id) {
        Quiz quiz = findQuiz(id);

        return QuizApplicationMapper.toDto(quiz);
    }

    private Quiz findQuiz(Long id) {
        return quizRepository.findBy(id)
                             .orElseThrow(() -> new QuizNotFoundException("지정한 id의 퀴즈를 찾지 못했습니다."));
    }

    private void publishAddedQuizQuestionEvent() {
        eventPublisher.publishEvent(new AddedQuizQuestionEvent());
    }

    private void validateQuizCreation(QuizCategory quizCategory) {
        WordMetadata wordMetadata = wordMetadataRepository.findBy(DEFAULT_WORD_METADATA_ID)
                                                          .orElseThrow(() -> new WordMetadataNotFoundException(
                                                                  "용어 메타데이터가 정상적으로 설정되지 않았습니다.")
                                                          );

        if (QuizWordCountValidator.isInvalidate(quizCategory, wordMetadata, REQUIRED_QUIZ_WORD_COUNT)) {
            throw new InvalidQuizWordCountException("퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
        }
    }

    private Quiz createQuiz(Long accountId, QuizCategory quizCategory) {
        Quiz quiz = new Quiz(accountId);
        Quiz savedQuiz = quizRepository.save(quiz);

        List<Word> words = findRandomWords(quizCategory);

        for (List<Word> splitWords : splitByQuestionWordCount(words)) {
            initQuizQuestion(splitWords, savedQuiz, quizCategory);
        }

        return quiz;
    }

    private List<Word> findRandomWords(QuizCategory quizCategory) {
        List<Long> wordIds = wordRandomRepository.findAllBy(quizCategory, REQUIRED_QUIZ_WORD_COUNT)
                                                 .stream()
                                                 .map(WordRandom::getWordId)
                                                 .toList();

        return wordRepository.findAllBy(wordIds);
    }

    private List<List<Word>> splitByQuestionWordCount(List<Word> words) {
        List<List<Word>> result = new ArrayList<>();

        for (int startIndex = 0; startIndex < REQUIRED_QUIZ_WORD_COUNT; startIndex += REQUIRED_QUESTION_WORD_COUNT) {
            int endIndex = Math.min(startIndex + REQUIRED_QUESTION_WORD_COUNT, REQUIRED_QUIZ_WORD_COUNT);

            result.add(words.subList(startIndex, endIndex));
        }

        return result;
    }

    private void initQuizQuestion(List<Word> splitWords, Quiz quiz, QuizCategory quizCategory) {
        Word answerWord = splitWords.get(ANSWER_OPTION_INDEX);
        QuizQuestion quizQuestion = createQuizQuestion(quiz, quizCategory, answerWord);

        quizQuestionRepository.save(quizQuestion);

        Collections.shuffle(splitWords);

        for (int i = 0; i < REQUIRED_QUESTION_WORD_COUNT; i++) {
            Word word = splitWords.get(i);
            QuizOption quizOption = QuizOption.of(word.getId(), word.getName(), i, quizQuestion);

            quizOptionRepository.save(quizOption);
        }
    }

    private QuizQuestion createQuizQuestion(Quiz quiz, QuizCategory quizCategory, Word answerWord) {
        QuizAnswerOption quizAnswerOption = new QuizAnswerOption(answerWord.getId(), answerWord.getName());

        return QuizQuestion.of(
                quizCategory,
                quizQuestionProperties.getQuestion(),
                answerWord.getWordMeaning().getMeaning(),
                quizAnswerOption,
                quiz
        );
    }
}
