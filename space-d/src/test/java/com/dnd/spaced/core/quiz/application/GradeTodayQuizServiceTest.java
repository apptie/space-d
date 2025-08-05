package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.exception.AlreadyGradeTodayQuizException;
import com.dnd.spaced.core.quiz.application.exception.TodayQuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.TodayQuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizGradedAnswerRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GradeTodayQuizServiceTest {

    private static final Long SELECTED_WORD_ID = 1L;
    private static final Long ACCOUNT_ID = 1L;
    private static final Long NOT_FOUND_TODAY_QUIZ_ID = -999L;
    private static final Long TODAY_QUIZ_ID = 1L;

    @Autowired
    TodayQuizServiceFacade todayQuizServiceFacade;

    @Autowired
    TodayQuizGradedAnswerRepository todayQuizGradedAnswerRepository;

    @Test
    void 지정한_오늘의_퀴즈_id가_없다면_퀴즈_정답을_제출할_수_없다() {
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
                () -> assertThat(actual.getAccountId()).isEqualTo(ACCOUNT_ID)
        );
    }
}
