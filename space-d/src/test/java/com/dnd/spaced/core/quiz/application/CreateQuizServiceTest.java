package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.exception.InvalidQuizWordCountException;
import com.dnd.spaced.core.quiz.application.exception.WordMetadataNotFoundException;
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
class CreateQuizServiceTest {

    private static final Long QUIZ_CREATOR_ID = 1L;

    @Autowired
    CreateQuizService createQuizService;

    @Test
    void 용어_메타데이터가_정상적으로_설정되지_않다면_퀴즈를_생성할_수_없다() {
        // given
        CreateQuizRequest request = new CreateQuizRequest("전체 실무");

        // when & then
        assertThatThrownBy(() -> createQuizService.createQuiz(QUIZ_CREATOR_ID, request))
                .isInstanceOf(WordMetadataNotFoundException.class)
                .hasMessage("용어 메타데이터가 정상적으로 설정되지 않았습니다.");
    }

    @Test
    @Sql("classpath:sql/quiz/word_metadata.sql")
    void 등록된_용어_수가_퀴즈_생성_시_필요한_용어_수보다_적으면_퀴즈를_생성할_수_없다() {
        // given
        CreateQuizRequest request = new CreateQuizRequest("전체 실무");

        // when & then
        assertThatThrownBy(() -> createQuizService.createQuiz(QUIZ_CREATOR_ID, request))
                .isInstanceOf(InvalidQuizWordCountException.class)
                .hasMessage("퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
    }

    @Test
    @Sql(scripts = {"classpath:sql/quiz/word_metadata.sql", "classpath:sql/quiz/word.sql"})
    void 퀴즈를_생성한다() {
        // given
        CreateQuizRequest request = new CreateQuizRequest("전체 실무");

        // when
        Long actual = createQuizService.createQuiz(QUIZ_CREATOR_ID, request);

        // then
        assertThat(actual).isPositive();
    }
}
