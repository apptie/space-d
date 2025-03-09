package com.dnd.spaced.core.skill.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.quiz.domain.QuizMetadata;
import com.dnd.spaced.core.quiz.domain.repository.QuizMetadataRepository;
import com.dnd.spaced.core.skill.application.dto.response.SkillResponse;
import com.dnd.spaced.core.skill.application.exception.QuizMetadataNotFoundException;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@CleanUpDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SkillServiceTest {

    @Autowired
    SkillService skillService;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    QuizMetadataRepository quizMetadataRepository;

    @Test
    void 퀴즈_메타데이터가_정상적으로_초기화되지_않았다면_스킬을_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> skillService.findBy(1L))
                .isInstanceOf(QuizMetadataNotFoundException.class)
                .hasMessage("퀴즈 메타데이터가 정상적으로 설정되지 않았습니다.");
    }

    @Test
    void 스킬_정보가_있을_때_스킬을_조회하면_회원이_퀴즈와_오늘의_퀴즈를_푼_통계_정보를_반환한다() {
        // given
        QuizMetadata quizMetadata = new QuizMetadata();
        ReflectionTestUtils.setField(quizMetadata, "totalQuizQuestionCount", 5L);
        quizMetadataRepository.save(quizMetadata);

        Skill skill = new Skill(1L);
        skill.addCorrectQuizQuestion(1L);
        skillRepository.save(skill);

        // when
        SkillResponse actual = skillService.findBy(1L);

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
    void 스킬_쩡보가_없을_때_스킬을_조회하면_통계_정보를_초기_값으로_반환한다() {
        // given
        QuizMetadata quizMetadata = new QuizMetadata();
        quizMetadataRepository.save(quizMetadata);

        // when
        SkillResponse actual = skillService.findBy(1L);

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
