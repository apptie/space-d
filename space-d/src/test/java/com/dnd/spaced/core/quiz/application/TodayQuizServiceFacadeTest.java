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
class TodayQuizServiceFacadeTest {

    private static final Long SELECTED_WORD_ID = 1L;
    private static final Long ACCOUNT_ID = 1L;
    private static final Long NOT_FOUND_TODAY_QUIZ_ID = -999L;
    private static final Long TODAY_QUIZ_ID = 1L;

    @Autowired
    ApplicationEvents events;

    @Autowired
    TodayQuizServiceFacade todayQuizServiceFacade;

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
        SimpleTodayQuizResponse actual = todayQuizServiceFacade.readLatestTodayQuiz();

        // then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.todayQuizQuestion()).isNotNull()
        );
    }

    @Test
    void 오늘의_퀴즈가_생성된_적이_없다면_최근에_생성한_오늘의_퀴즈를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> todayQuizServiceFacade.readLatestTodayQuiz())
                .isInstanceOf(TodayQuizNotFoundException.class)
                .hasMessage("오늘의 퀴즈가 생성되지 않았습니다.");
    }

    @Test
    void 지정한_오늘의_퀴즈_ID가_없다면_퀴즈_정답을_제출할_수_없다() {
        // when & then
        GradeTodayQuizRequest request = new GradeTodayQuizRequest(SELECTED_WORD_ID, "Authorization");

        assertThatThrownBy(() -> todayQuizServiceFacade.gradeTodayQuiz(ACCOUNT_ID, NOT_FOUND_TODAY_QUIZ_ID, request))
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
        GradeTodayQuizRequest request = new GradeTodayQuizRequest(SELECTED_WORD_ID, "Authorization");

        todayQuizServiceFacade.gradeTodayQuiz(ACCOUNT_ID, TODAY_QUIZ_ID, request);

        // then
        TodayQuizGradedAnswer actual = todayQuizGradedAnswerRepository.findBy(ACCOUNT_ID, TODAY_QUIZ_ID)
                                                                      .get();

        assertAll(
                () -> assertThat(actual.getTodayQuiz().getId()).isEqualTo(TODAY_QUIZ_ID),
                () -> assertThat(actual.getAccountId()).isEqualTo(ACCOUNT_ID),
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
    void 사용자가_제출한_모든_오늘의_퀴즈_채점_결과를_조회한다() {
        // given
        ReadTodayQuizGradedAnswerSearchRequest request = new ReadTodayQuizGradedAnswerSearchRequest(
                null
        );

        // when
        TodayQuizGradedAnswerCollectionResponse actual = todayQuizServiceFacade.readTodayQuizGradedAnswers(
                ACCOUNT_ID, request, PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual.answers()).hasSize(1),
                () -> assertThat(actual.answers().get(0).todayQuizId()).isEqualTo(TODAY_QUIZ_ID)
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
        GradeTodayQuizRequest request = new GradeTodayQuizRequest(SELECTED_WORD_ID, "Authorization");

        // when & then
        assertThatThrownBy(() -> todayQuizServiceFacade.gradeTodayQuiz(ACCOUNT_ID, TODAY_QUIZ_ID, request))
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
        TodayQuizGradedAnswerResponse actual = todayQuizServiceFacade.readTargetTodayQuizGradedAnswers(
                ACCOUNT_ID,
                TODAY_QUIZ_ID
        );

        // then
        assertAll(
                () -> assertThat(actual.todayQuizId()).isEqualTo(TODAY_QUIZ_ID),
                () -> assertThat(actual.accountId()).isEqualTo(ACCOUNT_ID)
        );
    }

    @Test
    void 지정한_오늘의_퀴즈_id가_없다면_사용자가_제출한_오늘의_퀴즈_채점_결과를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> todayQuizServiceFacade.readTargetTodayQuizGradedAnswers(ACCOUNT_ID, TODAY_QUIZ_ID))
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
        TodayQuizResponse actual = todayQuizServiceFacade.readTodayQuiz(ACCOUNT_ID, TODAY_QUIZ_ID);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(TODAY_QUIZ_ID),
                () -> assertThat(actual.todayQuizQuestion()).isNotNull()
        );
    }

    @Test
    void 없는_ID의_오늘의_퀴즈를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> todayQuizServiceFacade.readTodayQuiz(ACCOUNT_ID, NOT_FOUND_TODAY_QUIZ_ID))
                .isInstanceOf(TodayQuizNotFoundException.class)
                .hasMessage("지정한 id의 오늘의 퀴즈를 찾지 못했습니다.");
    }
}
