package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.quiz.application.dto.request.ReadAllQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.exception.QuizNotFoundException;
import com.dnd.spaced.core.quiz.domain.QuizGradedAnswer;
import com.dnd.spaced.core.quiz.domain.dto.QuizDto;
import com.dnd.spaced.core.quiz.domain.dto.SimpleQuizDto;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReadQuizServiceTest {

    private static final Long QUIZ_CREATOR_ID = 1L;
    private static final Long UNSOLVED_QUIZ_ID = 1L;
    private static final Long SOLVED_QUIZ_ID = 2L;
    private static final Long NOT_FOUND_QUIZ_ID = -999L;
    private static final Long NON_QUIZ_CREATOR_ID = 5L;

    @Autowired
    ReadQuizService readQuizService;

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql"
    })
    void 퀴즈를_조회한다() {
        // when
        QuizDto actual = readQuizService.readQuiz(QUIZ_CREATOR_ID, UNSOLVED_QUIZ_ID);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(UNSOLVED_QUIZ_ID),
                () -> assertThat(actual.accountId()).isEqualTo(QUIZ_CREATOR_ID),
                () -> assertThat(actual.quizQuestions()).hasSize(5),
                () -> assertThat(actual.quizQuestions().get(0).quizOptions()).hasSize(4),
                () -> assertThat(actual.quizQuestions().get(1).quizOptions()).hasSize(4),
                () -> assertThat(actual.quizQuestions().get(2).quizOptions()).hasSize(4),
                () -> assertThat(actual.quizQuestions().get(3).quizOptions()).hasSize(4),
                () -> assertThat(actual.quizQuestions().get(4).quizOptions()).hasSize(4)
        );
    }

    @Test
    void 유효하지_않는_퀴즈_ID로_퀴즈를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> readQuizService.readQuiz(QUIZ_CREATOR_ID, NOT_FOUND_QUIZ_ID))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/quiz.sql"
    })
    void 회원이_생성한_퀴즈가_아니라면_존재하는_퀴즈_ID더라도_퀴즈_정보를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> readQuizService.readQuiz(NON_QUIZ_CREATOR_ID, UNSOLVED_QUIZ_ID))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql",
            "classpath:sql/quiz/quiz_graded_answer.sql"
    })
    void 모든_퀴즈의_제출했던_답을_조회한다() {
        // given
        ReadQuizGradedAnswerSearchRequest request = new ReadQuizGradedAnswerSearchRequest(null);

        // when
        List<QuizGradedAnswer> actual = readQuizService.readGradedAnswers(
                1L, request, PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(5),
                () -> assertThat(actual.get(0).getSelectedContent()).isNotBlank(),
                () -> assertThat(actual.get(1).getSelectedContent()).isNotBlank(),
                () -> assertThat(actual.get(2).getSelectedContent()).isNotBlank(),
                () -> assertThat(actual.get(3).getSelectedContent()).isNotBlank(),
                () -> assertThat(actual.get(4).getSelectedContent()).isNotBlank()
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql",
            "classpath:sql/quiz/quiz_graded_answer.sql"
    })
    void 특정_퀴즈의_제출했던_답을_조회한다() {
        // when
        List<QuizGradedAnswer> actual = readQuizService.readGradedAnswers(QUIZ_CREATOR_ID, SOLVED_QUIZ_ID);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(5),
                () -> assertThat(actual.get(0).getSelectedContent()).isNotBlank(),
                () -> assertThat(actual.get(1).getSelectedContent()).isNotBlank(),
                () -> assertThat(actual.get(2).getSelectedContent()).isNotBlank(),
                () -> assertThat(actual.get(3).getSelectedContent()).isNotBlank(),
                () -> assertThat(actual.get(4).getSelectedContent()).isNotBlank()
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql"
    })
    void 회원이_생성한_퀴즈_목록을_조회한다() {
        // given
        ReadAllQuizRequest request = new ReadAllQuizRequest(null);

        // when
        List<SimpleQuizDto> actual = readQuizService.readQuizzes(QUIZ_CREATOR_ID, request, Pageable.ofSize(10));

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).id()).isEqualTo(UNSOLVED_QUIZ_ID)
        );
    }
}
