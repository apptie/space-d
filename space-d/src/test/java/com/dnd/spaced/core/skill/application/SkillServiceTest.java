package com.dnd.spaced.core.skill.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.skill.application.dto.response.SkillResponse;
import com.dnd.spaced.core.skill.application.exception.QuizMetadataNotFoundException;
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
class SkillServiceTest {

    @Autowired
    SkillService skillService;

    @Test
    void 퀴즈_메타데이터가_정상적으로_초기화되지_않았다면_스킬을_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> skillService.readSkill(1L))
                .isInstanceOf(QuizMetadataNotFoundException.class)
                .hasMessage("퀴즈 메타데이터가 정상적으로 설정되지 않았습니다.");
    }

    @Test
    @Sql(value = {
            "classpath:sql/skill/quiz_metadata.sql",
            "classpath:sql/skill/skill.sql"
    })
    void 스킬_정보가_있을_때_스킬을_조회하면_회원이_퀴즈와_오늘의_퀴즈를_푼_통계_정보를_반환한다() {
        // when
        SkillResponse actual = skillService.readSkill(1L);

        // then
        assertAll(
                () -> assertThat(actual.accountId()).isEqualTo(1L),
                () -> assertThat(actual.quizQuestionCorrectCount()).isEqualTo(1L),
                () -> assertThat(actual.submitQuizQuestionCount()).isEqualTo(5L),
                () -> assertThat(actual.todayQuizQuestionCorrectCount()).isEqualTo(0L),
                () -> assertThat(actual.submitTodayQuizQuestionCount()).isEqualTo(0L),
                () -> assertThat(actual.totalQuizQuestionCorrectPercent()).isEqualTo(20.0d),
                () -> assertThat(actual.totalTodayQuizQuestionCorrectPercent()).isEqualTo(0.0d)
        );
    }

    @Test
    @Sql("classpath:sql/skill/quiz_metadata.sql")
    void 스킬_정보가_없을_때_스킬을_조회하면_통계_정보를_초기_값으로_반환한다() {
        // when
        SkillResponse actual = skillService.readSkill(1L);

        // then
        assertAll(
                () -> assertThat(actual.accountId()).isEqualTo(1L),
                () -> assertThat(actual.quizQuestionCorrectCount()).isEqualTo(0L),
                () -> assertThat(actual.submitQuizQuestionCount()).isEqualTo(0L),
                () -> assertThat(actual.todayQuizQuestionCorrectCount()).isEqualTo(0L),
                () -> assertThat(actual.submitTodayQuizQuestionCount()).isEqualTo(0L),
                () -> assertThat(actual.totalQuizQuestionCorrectPercent()).isEqualTo(0.0d),
                () -> assertThat(actual.totalTodayQuizQuestionCorrectPercent()).isEqualTo(0.0d)
        );
    }
}
