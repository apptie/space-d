package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.ReadTodayQuizDto;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReadTodayQuizServiceTest {

    @Autowired
    ReadTodayQuizService todayQuizService;

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager.getCacheNames()
                    .forEach(name -> cacheManager.getCache(name).clear());
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/today_quiz.sql"
    })
    void 최근에_생성한_오늘의_퀴즈를_조회한다() {
        // when
        SimpleTodayQuizDto actual = todayQuizService.readLatestTodayQuiz();

        // then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.todayQuizAnswerOption()).isNotNull()
        );
    }

    @Test
    void 오늘의_퀴즈가_생성된_적이_없다면_최근에_생성한_오늘의_퀴즈를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> todayQuizService.readLatestTodayQuiz())
                .isInstanceOf(TodayQuizNotFoundException.class)
                .hasMessage("오늘의 퀴즈가 생성되지 않았습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/today_quiz.sql",
            "classpath:sql/quiz/today_quiz_graded_answer.sql"
    })
    void 사용자가_제출한_모든_오늘의_퀴즈_채점_결과를_조회한다() {
        // given
        ReadTodayQuizGradedAnswerSearchRequest request = new ReadTodayQuizGradedAnswerSearchRequest(
                null
        );

        // when
        List<TodayQuizGradedAnswer> actual = todayQuizService.readTodayQuizGradedAnswers(
                1L, request, PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getTodayQuiz().getId()).isEqualTo(1L)
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/today_quiz.sql",
            "classpath:sql/quiz/today_quiz_graded_answer.sql"
    })
    void 사용자가_제출한_오늘의_퀴즈_채점_결과를_조회한다() {
        // when
        TodayQuizGradedAnswer actual = todayQuizService.readTargetTodayQuizGradedAnswers(
                1L,
                1L
        );

        // then
        assertAll(
                () -> assertThat(actual.getTodayQuiz().getId()).isEqualTo(1L),
                () -> assertThat(actual.getAccountId()).isEqualTo(1L)
        );
    }

    @Test
    void 지정한_오늘의_퀴즈_id가_없다면_사용자가_제출한_오늘의_퀴즈_채점_결과를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> todayQuizService.readTargetTodayQuizGradedAnswers(1L, 1L))
                .isInstanceOf(TodayQuizNotFoundException.class)
                .hasMessage("지정한 오늘의 퀴즈 답안지를 찾지 못했습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/today_quiz.sql"
    })
    void 지정한_id의_오늘의_퀴즈를_조회한다() {
        // when
        ReadTodayQuizDto actual = todayQuizService.readTodayQuiz(1L, 1L);

        // then
        assertAll(
                () -> assertThat(actual.todayQuiz().getId()).isEqualTo(1L),
                () -> assertThat(actual.todayQuiz().getTodayQuizQuestion()).isNotNull()
        );
    }

    @Test
    void 없는_id의_오늘의_퀴즈를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> todayQuizService.readTodayQuiz(1L, -999L))
                .isInstanceOf(TodayQuizNotFoundException.class)
                .hasMessage("지정한 id의 오늘의 퀴즈를 찾지 못했습니다.");
    }
}
