package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest.SubmitAnswerRequest;
import com.dnd.spaced.core.quiz.application.exception.AlreadyGradeQuizException;
import com.dnd.spaced.core.quiz.application.exception.QuizNotFoundException;
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
class GradeQuizServiceTest {

    @Autowired
    GradeQuizService gradeQuizService;

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
        assertThatThrownBy(() -> gradeQuizService.gradeQuiz(1L, -999L, request))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    @Sql(scripts = {
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
        assertDoesNotThrow(() -> gradeQuizService.gradeQuiz(1L, 1L, request));
    }

    @Test
    @Sql(scripts = {
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
        assertThatThrownBy(() -> gradeQuizService.gradeQuiz(1L, 1L, request))
                .isInstanceOf(AlreadyGradeQuizException.class)
                .hasMessage("이미 풀었던 퀴즈입니다.");
    }
}
