package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.exception.AlreadyGradeTodayQuizException;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizGradedAnswerRepository;
import com.dnd.spaced.core.skill.application.event.dto.GradedTodayQuizEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TodayQuizServiceTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    TodayQuizService todayQuizService;

    @Autowired
    TodayQuizGradedAnswerRepository todayQuizGradedAnswerRepository;

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
        SimpleTodayQuizResponse actual = todayQuizService.readLatestTodayQuiz();

        // then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.todayQuizQuestion()).isNotNull()
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
    void 지정한_오늘의_퀴즈_id가_없다면_퀴즈_정답을_제출할_수_없다() {
        // when & then
        GradeTodayQuizRequest request = new GradeTodayQuizRequest(1L, "Authorization");

        assertThatThrownBy(() -> todayQuizService.grade(1L, -999L, request))
                .isInstanceOf(TodayQuizNotFoundException.class)
                .hasMessage("지정한 id의 오늘의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/today_quiz.sql"
    })
    void 오늘의_퀴즈_정답을_제출한다() {
        // when
        GradeTodayQuizRequest request = new GradeTodayQuizRequest(1L, "Authorization");

        todayQuizService.grade(1L, 1L, request);

        // then
        TodayQuizGradedAnswer actual = todayQuizGradedAnswerRepository.findBy(1L, 1L)
                                                                      .get();

        assertAll(
                () -> assertThat(actual.getTodayQuiz().getId()).isEqualTo(1L),
                () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                () -> assertThat(events.stream(GradedTodayQuizEvent.class).count()).isOne()
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/today_quiz.sql",
            "classpath:sql/quiz/today_quiz_graded_answer.sql"
    })
    void 사용자가_제출한_모든_오늘의_퀴즈_채점_결과를_반환한다() {
        // given
        ReadTodayQuizGradedAnswerSearchRequest request = new ReadTodayQuizGradedAnswerSearchRequest(
                null
        );

        // when
        TodayQuizGradedAnswerCollectionResponse actual = todayQuizService.readTodayQuizGradedAnswers(
                1L, request, PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual.answers()).hasSize(1),
                () -> assertThat(actual.answers().get(0).todayQuizId()).isEqualTo(1L)
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/today_quiz.sql",
            "classpath:sql/quiz/today_quiz_graded_answer.sql"
    })
    void 이미_푼_오늘의_퀴즈인_경우_정답을_제출할_수_없다() {
        // given
        GradeTodayQuizRequest request = new GradeTodayQuizRequest(1L, "Authorization");

        // when & then
        assertThatThrownBy(() -> todayQuizService.grade(1L, 1L, request))
                .isInstanceOf(AlreadyGradeTodayQuizException.class)
                .hasMessage("이미 오늘의 퀴즈를 풀었습니다.");
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
        TodayQuizGradedAnswerResponse actual = todayQuizService.readTargetTodayQuizGradedAnswers(
                1L,
                1L
        );

        // then
        assertAll(
                () -> assertThat(actual.todayQuizId()).isEqualTo(1L),
                () -> assertThat(actual.accountId()).isEqualTo(1L)
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
        TodayQuizResponse actual = todayQuizService.readTodayQuiz(1L, 1L);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(1L),
                () -> assertThat(actual.todayQuizQuestion()).isNotNull()
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
