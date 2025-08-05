package com.dnd.spaced.core.skill.application.event.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.dnd.spaced.core.quiz.application.QuizServiceFacade;
import com.dnd.spaced.core.quiz.application.TodayQuizServiceFacade;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest.SubmitAnswerRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.skill.application.event.dto.FailedGradedQuizSkillEvent;
import com.dnd.spaced.core.skill.application.event.dto.FailedGradedTodayQuizSkillEvent;
import com.dnd.spaced.core.skill.application.event.dto.GradedQuizEvent;
import com.dnd.spaced.core.skill.application.event.dto.GradedTodayQuizEvent;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GradedQuizEventListenerTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    QuizServiceFacade quizServiceFacade;

    @Autowired
    TodayQuizServiceFacade todayQuizServiceFacade;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    RedisTemplate<String, FailedGradedQuizSkillEvent> gradedQuizEventFailedRedisTemplate;

    @Autowired
    RedisTemplate<String, FailedGradedTodayQuizSkillEvent> gradedTodayQuizEventFailedRedisTemplate;

    @Test
    @Sql("classpath:sql/skill/quiz.sql")
    void 퀴즈_정답지_제출_이후_퀴즈_정답지_제출_이벤트를_수행한다() {
        // given
        Skill spySkill = spy(Skill.class);

        given(skillRepository.findBy(anyLong())).willReturn(Optional.of(spySkill));

        SubmitAnswerRequest[] submitAnswers = {
                new SubmitAnswerRequest(1L, "Authorization"),
                new SubmitAnswerRequest(5L, "Dequeue"),
                new SubmitAnswerRequest(9L, "usage"),
                new SubmitAnswerRequest(13L, "empty"),
                new SubmitAnswerRequest(17L, "execute")
        };
        GradeQuizRequest request = new GradeQuizRequest(submitAnswers);

        // when
        quizServiceFacade.grade(1L, 1L, request);

        // then
        assertAll(
                () -> assertThat(events.stream(GradedQuizEvent.class).count()).isOne(),
                () -> verify(skillRepository).findBy(anyLong()),
                () -> verify(spySkill).addCorrectQuizQuestion(anyLong()),
                () -> verify(gradedQuizEventFailedRedisTemplate, never()).opsForList(),
                () -> verify(gradedTodayQuizEventFailedRedisTemplate, never()).opsForList()
        );
    }

    @Test
    @Sql("classpath:sql/skill/quiz.sql")
    void 퀴즈_정답지_제출_이후_퀴즈_정답지_제출_이벤트_처리에_실패하더라도_최대_재시도_횟수만큼_이벤트_처리를_재시도한다() {
        // given
        Skill spySkill = spy(Skill.class);

        doThrow(DataAccessResourceFailureException.class).doThrow(DataAccessResourceFailureException.class)
                                                         .doReturn(Optional.of(spySkill))
                                                         .when(skillRepository)
                                                         .findBy(anyLong());

        SubmitAnswerRequest[] submitAnswers = {
                new SubmitAnswerRequest(1L, "Authorization"),
                new SubmitAnswerRequest(5L, "Dequeue"),
                new SubmitAnswerRequest(9L, "usage"),
                new SubmitAnswerRequest(13L, "empty"),
                new SubmitAnswerRequest(17L, "execute")
        };
        GradeQuizRequest request = new GradeQuizRequest(submitAnswers);

        // when
        quizServiceFacade.grade(1L, 1L, request);

        // then
        assertAll(
                () -> assertThat(events.stream(GradedQuizEvent.class).count()).isOne(),
                () -> verify(skillRepository, times(3)).findBy(anyLong()),
                () -> verify(spySkill).addCorrectQuizQuestion(anyLong()),
                () -> verify(gradedQuizEventFailedRedisTemplate, never()).opsForList(),
                () -> verify(gradedTodayQuizEventFailedRedisTemplate, never()).opsForList()
        );
    }

    @Test
    @Sql("classpath:sql/skill/quiz.sql")
    void 퀴즈_정답지_제출_이후_최대_재시도_횟수보다_더_이벤트_처리에_실패한_횟수가_많다면_실패한_이벤트를_별도로_관리한다() {
        // given
        doThrow(DataAccessResourceFailureException.class).doThrow(DataAccessResourceFailureException.class)
                                                         .doThrow(DataAccessResourceFailureException.class)
                                                         .when(skillRepository)
                                                         .findBy(anyLong());

        SubmitAnswerRequest[] submitAnswers = {
                new SubmitAnswerRequest(1L, "Authorization"),
                new SubmitAnswerRequest(5L, "Dequeue"),
                new SubmitAnswerRequest(9L, "usage"),
                new SubmitAnswerRequest(13L, "empty"),
                new SubmitAnswerRequest(17L, "execute")
        };
        GradeQuizRequest request = new GradeQuizRequest(submitAnswers);

        // when
        quizServiceFacade.grade(1L, 1L, request);

        // then
        assertAll(
                () -> assertThat(events.stream(GradedQuizEvent.class).count()).isOne(),
                () -> verify(skillRepository, times(3)).findBy(anyLong()),
                () -> verify(gradedQuizEventFailedRedisTemplate).opsForList(),
                () -> verify(gradedTodayQuizEventFailedRedisTemplate, never()).opsForList()
        );
    }

    @Test
    @Sql("classpath:sql/skill/today_quiz.sql")
    void 오늘의_퀴즈_정답지_제출_이후_퀴즈_정답지_제출_이벤트를_수행한다() {
        // given
        Skill spySkill = spy(Skill.class);

        given(skillRepository.findBy(anyLong())).willReturn(Optional.of(spySkill));

        GradeTodayQuizRequest request = new GradeTodayQuizRequest(2L, "YAML");

        // when
        todayQuizServiceFacade.gradeTodayQuiz(1L, 1L, request);

        // then
        assertAll(
                () -> assertThat(events.stream(GradedTodayQuizEvent.class).count()).isOne(),
                () -> verify(skillRepository).findBy(anyLong()),
                () -> verify(spySkill).addCorrectTodayQuizQuestion(anyLong()),
                () -> verify(gradedQuizEventFailedRedisTemplate, never()).opsForList(),
                () -> verify(gradedTodayQuizEventFailedRedisTemplate, never()).opsForList()
        );
    }

    @Test
    @Sql("classpath:sql/skill/today_quiz.sql")
    void 오늘의_퀴즈_정답지_제출_이후_퀴즈_정답지_제출_이벤트_처리에_실패하더라도_최대_재시도_횟수만큼_이벤트_처리를_재시도한다() {
        // given
        Skill spySkill = spy(Skill.class);

        doThrow(DataAccessResourceFailureException.class).doThrow(DataAccessResourceFailureException.class)
                                                         .doReturn(Optional.of(spySkill))
                                                         .when(skillRepository)
                                                         .findBy(anyLong());

        GradeTodayQuizRequest request = new GradeTodayQuizRequest(2L, "YAML");

        // when
        todayQuizServiceFacade.gradeTodayQuiz(1L, 1L, request);

        // then
        assertAll(
                () -> assertThat(events.stream(GradedTodayQuizEvent.class).count()).isOne(),
                () -> verify(skillRepository, times(3)).findBy(anyLong()),
                () -> verify(spySkill).addCorrectTodayQuizQuestion(anyLong()),
                () -> verify(gradedQuizEventFailedRedisTemplate, never()).opsForList(),
                () -> verify(gradedTodayQuizEventFailedRedisTemplate, never()).opsForList()
        );
    }

    @Test
    @Sql("classpath:sql/skill/today_quiz.sql")
    void 오늘의_퀴즈_정답지_제출_이후_최대_재시도_횟수보다_더_이벤트_처리에_실패한_횟수가_많다면_실패한_이벤트를_별도로_관리한다() {
        // given
        doThrow(DataAccessResourceFailureException.class).doThrow(DataAccessResourceFailureException.class)
                                                         .doThrow(DataAccessResourceFailureException.class)
                                                         .when(skillRepository)
                                                         .findBy(anyLong());

        GradeTodayQuizRequest request = new GradeTodayQuizRequest(2L, "YAML");

        // when
        todayQuizServiceFacade.gradeTodayQuiz(1L, 1L, request);

        // then
        assertAll(
                () -> assertThat(events.stream(GradedTodayQuizEvent.class).count()).isOne(),
                () -> verify(skillRepository, times(3)).findBy(anyLong()),
                () -> verify(gradedQuizEventFailedRedisTemplate, never()).opsForList(),
                () -> verify(gradedTodayQuizEventFailedRedisTemplate).opsForList()
        );
    }
}
