package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.AdminWordService;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.application.helper.WithTodayQuizTestHelper;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizGradedAnswerRepository;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.dnd.spaced.core.skill.application.event.dto.GradedTodayQuizEvent;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanUpDatabase
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TodayQuizServiceTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    TodayQuizService todayQuizService;

    @Autowired
    AdminWordService adminWordService;

    @Autowired
    TodayQuizRepository todayQuizRepository;

    @Autowired
    WordMetadataRepository wordMetadataRepository;

    @Autowired
    TodayQuizGradedAnswerRepository todayQuizGradedAnswerRepository;

    @Nested
    class WithoutMetadataTest {

        @Test
        void 지정한_오늘의_퀴즈_id가_없다면_퀴즈_정답을_제출할_수_없다() {
            // when & then
            GradeTodayQuizRequest request = new GradeTodayQuizRequest(1);

            assertThatThrownBy(() -> todayQuizService.grade(1L, -999L, request))
                    .isInstanceOf(TodayQuizNotFoundException.class)
                    .hasMessage("지정한 id의 오늘의 퀴즈를 찾지 못했습니다.");
        }

        @Test
        void 오늘의_퀴즈가_생성된_적이_없다면_최근에_생성한_오늘의_퀴즈를_조회할_수_없다() {
            // when & then
            assertThatThrownBy(() -> todayQuizService.findLatest())
                    .isInstanceOf(TodayQuizNotFoundException.class)
                    .hasMessage("오늘의 퀴즈가 생성되지 않았습니다.");
        }
    }

    @Nested
    class WithTodayQuizTest extends WithTodayQuizTestHelper {

        @Test
        void 최근에_생성한_오늘의_퀴즈를_조회한다() {
            // when
            TodayQuizResponse actual = todayQuizService.findLatest();

            // then
            assertAll(
                    () -> assertThat(actual.id()).isPositive(),
                    () -> assertThat(actual.todayQuizQuestion()).isNotNull(),
                    () -> assertThat(actual.todayQuizQuestion().todayQuizOptions()).hasSize(4)
            );
        }

        @Test
        void 오늘의_퀴즈_정답을_제출한다() {
            // when
            GradeTodayQuizRequest request = new GradeTodayQuizRequest(1);

            todayQuizService.grade(1L, todayQuiz.getId(), request);

            // then
            TodayQuizGradedAnswer actual = todayQuizGradedAnswerRepository.findBy(1L, todayQuiz.getId())
                                                                          .get();

            assertAll(
                    () -> assertThat(actual.getTodayQuiz().getId()).isEqualTo(todayQuiz.getId()),
                    () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                    () -> assertThat(actual.getSelectedOptionIndex()).isEqualTo(1),
                    () -> assertThat(events.stream(GradedTodayQuizEvent.class).count()).isOne()
            );
        }

        @Test
        void 사용자가_제출한_모든_오늘의_퀴즈_채점_결과를_반환한다() {
            // given
            GradeTodayQuizRequest gradeTodayQuizRequest = new GradeTodayQuizRequest(1);
            todayQuizService.grade(1L, todayQuiz.getId(), gradeTodayQuizRequest);

            // when
            ReadTodayQuizGradedAnswerSearchRequest request = new ReadTodayQuizGradedAnswerSearchRequest(
                    null
            );
            TodayQuizGradedAnswerCollectionResponse actual = todayQuizService.findTodayQuizGradedAnswerAllBy(
                    1L, request, PageRequest.of(0, 10)
            );

            // then
            assertAll(
                    () -> assertThat(actual.answers()).hasSize(1),
                    () -> assertThat(actual.answers().get(0).todayQuizId()).isEqualTo(todayQuiz.getId())
            );
        }

        @Test
        void 사용자가_제출한_오늘의_퀴즈_채점_결과를_조회한다() {
            // given
            GradeTodayQuizRequest request = new GradeTodayQuizRequest(1);
            todayQuizService.grade(1L, todayQuiz.getId(), request);

            // when
            TodayQuizGradedAnswerResponse actual = todayQuizService.findTodayQuizGradedAnswerBy(
                    1L,
                    todayQuiz.getId()
            );

            // then
            assertAll(
                    () -> assertThat(actual.todayQuizId()).isEqualTo(todayQuiz.getId()),
                    () -> assertThat(actual.accountId()).isEqualTo(1L)
            );
        }

        @Test
        void 지정한_오늘의_퀴즈_id가_없다면_사용자가_제출한_오늘의_퀴즈_채점_결과를_조회할_수_없다() {
            // when & then
            assertThatThrownBy(() -> todayQuizService.findTodayQuizGradedAnswerBy(1L, 1L))
                    .isInstanceOf(TodayQuizNotFoundException.class)
                    .hasMessage("지정한 id의 오늘의 퀴즈를 찾지 못했습니다.");
        }

        @Test
        void 지정한_id의_오늘의_퀴즈를_조회한다() {
            // given
            TodayQuiz savedTodayQuiz = todayQuizRepository.save(todayQuiz);

            // when
            TodayQuizResponse actual = todayQuizService.findBy(savedTodayQuiz.getId());

            // then
            assertAll(
                    () -> assertThat(actual.id()).isPositive(),
                    () -> assertThat(actual.todayQuizQuestion()).isNotNull(),
                    () -> assertThat(actual.todayQuizQuestion().todayQuizOptions()).hasSize(4)
            );
        }

        @Test
        void 없는_id의_오늘의_퀴즈를_조회할_수_없다() {
            // when & then
            assertThatThrownBy(() -> todayQuizService.findBy(-999L))
                    .isInstanceOf(TodayQuizNotFoundException.class)
                    .hasMessage("지정한 id의 오늘의 퀴즈를 찾지 못했습니다.");
        }
    }
}
