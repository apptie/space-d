package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest.SubmitAnswerRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadAllQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.QuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.event.dto.AddedQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.exception.AlreadyGradeQuizException;
import com.dnd.spaced.core.quiz.application.exception.InvalidQuizWordCountException;
import com.dnd.spaced.core.quiz.application.exception.QuizNotFoundException;
import com.dnd.spaced.core.quiz.application.exception.WordMetadataNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizServiceTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    QuizService quizService;

    @Test
    @Sql("classpath:sql/cleanup.sql")
    void 용어_메타데이터가_정상적으로_설정되지_않다면_퀴즈를_생성할_수_없다() {
        // given
        CreateQuizRequest request = new CreateQuizRequest("전체 실무");

        // when & then
        assertThatThrownBy(() -> quizService.createQuiz(1L, request))
                .isInstanceOf(WordMetadataNotFoundException.class)
                .hasMessage("용어 메타데이터가 정상적으로 설정되지 않았습니다.");
    }

    @Test
    @Sql(scripts = {"classpath:sql/cleanup.sql", "classpath:sql/quiz/word_metadata.sql"})
    void 등록된_용어_수가_퀴즈_생성_시_필요한_용어_수보다_적으면_퀴즈를_생성할_수_없다() {
        // given
        CreateQuizRequest request = new CreateQuizRequest("전체 실무");

        // when & then
        assertThatThrownBy(() -> quizService.createQuiz(1L, request))
                .isInstanceOf(InvalidQuizWordCountException.class)
                .hasMessage("퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql"
    })
    void 퀴즈를_조회한다() {
        // when
        QuizResponse actual = quizService.readQuiz(1L, 1L);

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(1L),
                () -> assertThat(actual.accountId()).isEqualTo(1L),
                () -> assertThat(actual.quizQuestions()).hasSize(5),
                () -> assertThat(actual.quizQuestions().get(0).quizOptions()).hasSize(4),
                () -> assertThat(actual.quizQuestions().get(1).quizOptions()).hasSize(4),
                () -> assertThat(actual.quizQuestions().get(2).quizOptions()).hasSize(4),
                () -> assertThat(actual.quizQuestions().get(3).quizOptions()).hasSize(4),
                () -> assertThat(actual.quizQuestions().get(4).quizOptions()).hasSize(4)
        );
    }

    @Test
    @Sql(scripts = {"classpath:sql/cleanup.sql", "classpath:sql/quiz/word_metadata.sql", "classpath:sql/quiz/word.sql"})
    void 퀴즈를_생성한다() {
        // given
        CreateQuizRequest request = new CreateQuizRequest("전체 실무");

        // when
        Long actual = quizService.createQuiz(1L, request);

        // then
        assertAll(
                () -> assertThat(actual).isPositive(),
                () -> assertThat(events.stream(AddedQuizQuestionEvent.class).count()).isOne()
        );
    }

    @Test
    void 유효하지_않는_퀴즈_id로_퀴즈를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> quizService.readQuiz(1L, -999L))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    @Sql(scripts = {"classpath:sql/cleanup.sql", "classpath:sql/quiz/word_metadata.sql", "classpath:sql/quiz/quiz.sql"})
    void 회원이_생성한_퀴즈가_아니라면_존재하는_퀴즈_id더라도_퀴즈_정보를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> quizService.readQuiz(5L, 1L))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    void 유효하지_않는_퀴즈_id로_퀴즈_답을_제출할_수_없다() {
        // given
        SubmitAnswerRequest[] submitAnswers = {
                new SubmitAnswerRequest(1L, "Authorization"),
                new SubmitAnswerRequest(2L, "Domain"),
                new SubmitAnswerRequest(3L, "Controller"),
                new SubmitAnswerRequest(2L, "Web"),
                new SubmitAnswerRequest(1L, "HTTP")
        };
        GradeQuizRequest request = new GradeQuizRequest(submitAnswers);

        // when & then
        assertThatThrownBy(() -> quizService.grade(1L, -999L, request))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql"
    })
    void 퀴즈_정답을_제출한다() {
        // given
        SubmitAnswerRequest[] submitAnswers = {
                new SubmitAnswerRequest(1L, "Authorization"),
                new SubmitAnswerRequest(2L, "Domain"),
                new SubmitAnswerRequest(3L, "Controller"),
                new SubmitAnswerRequest(2L, "Web"),
                new SubmitAnswerRequest(1L, "HTTP")
        };
        GradeQuizRequest request = new GradeQuizRequest(submitAnswers);

        // when & then
        assertDoesNotThrow(() -> quizService.grade(1L, 1L, request));
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/solved_quiz.sql"
    })
    void 이미_푼_퀴즈인_경우_정답을_제출할_수_없다() {
        // given
        SubmitAnswerRequest[] submitAnswers = {
                new SubmitAnswerRequest(1L, "Authorization"),
                new SubmitAnswerRequest(2L, "Domain"),
                new SubmitAnswerRequest(3L, "Controller"),
                new SubmitAnswerRequest(2L, "Web"),
                new SubmitAnswerRequest(1L, "HTTP")
        };
        GradeQuizRequest request = new GradeQuizRequest(submitAnswers);

        // when & then
        assertThatThrownBy(() -> quizService.grade(1L, 1L, request))
                .isInstanceOf(AlreadyGradeQuizException.class)
                .hasMessage("이미 풀었던 퀴즈입니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql",
            "classpath:sql/quiz/quiz_graded_answer.sql"
    })
    void 모든_퀴즈의_제출했던_답을_조회한다() {
        // given
        ReadQuizGradedAnswerSearchRequest request = new ReadQuizGradedAnswerSearchRequest(null);

        // when
        QuizGradedAnswerCollectionResponse actual = quizService.readGradedAnswers(
                1L, request, PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual.answers()).hasSize(5),
                () -> assertThat(actual.answers().get(0).selectedQuizOptionContent()).isNotBlank(),
                () -> assertThat(actual.answers().get(1).selectedQuizOptionContent()).isNotBlank(),
                () -> assertThat(actual.answers().get(2).selectedQuizOptionContent()).isNotBlank(),
                () -> assertThat(actual.answers().get(3).selectedQuizOptionContent()).isNotBlank(),
                () -> assertThat(actual.answers().get(4).selectedQuizOptionContent()).isNotBlank()
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql",
            "classpath:sql/quiz/quiz_graded_answer.sql"
    })
    void 특정_퀴즈의_제출했던_답을_조회한다() {
        // when
        QuizGradedAnswerCollectionResponse actual = quizService.readGradedAnswers(1L, 1L);

        // then
        assertAll(
                () -> assertThat(actual.answers()).hasSize(5),
                () -> assertThat(actual.answers().get(0).selectedQuizOptionContent()).isNotBlank(),
                () -> assertThat(actual.answers().get(1).selectedQuizOptionContent()).isNotBlank(),
                () -> assertThat(actual.answers().get(2).selectedQuizOptionContent()).isNotBlank(),
                () -> assertThat(actual.answers().get(3).selectedQuizOptionContent()).isNotBlank(),
                () -> assertThat(actual.answers().get(4).selectedQuizOptionContent()).isNotBlank()
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/quiz/word_metadata.sql",
            "classpath:sql/quiz/word.sql",
            "classpath:sql/quiz/quiz.sql"
    })
    void 회원이_생성한_퀴즈_목록을_조회한다() {
        // given
        ReadAllQuizRequest request = new ReadAllQuizRequest(null);

        // when
        QuizCollectionResponse actual = quizService.readQuizzes(1L, request, Pageable.ofSize(10));

        // then
        assertAll(
                () -> assertThat(actual.quizzes()).hasSize(1),
                () -> assertThat(actual.lastQuizId()).isEqualTo(1L)
        );
    }
}
