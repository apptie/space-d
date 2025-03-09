package com.dnd.spaced.core.skill.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SkillTest {

    @Test
    void 스킬을_초기화한다() {
        // when & then
        Skill actual = assertDoesNotThrow(() -> new Skill(1L));

        assertAll(
                () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.getQuizQuestionCorrectCount()).isZero(),
                () -> assertThat(actual.getTodayQuizQuestionCorrectCount()).isZero(),
                () -> assertThat(actual.getSubmitQuizQuestionCount()).isZero(),
                () -> assertThat(actual.getSubmitTodayQuizQuestionCount()).isZero()
        );
    }

    @Test
    void 스킬에_퀴즈_문제_제출_결과를_적용한다() {
        // given
        Skill skill = new Skill(1L);

        // when
        skill.addCorrectQuizQuestion(1L);

        // then
        assertAll(
                () -> assertThat(skill.getAccountId()).isEqualTo(1L),
                () -> assertThat(skill.getQuizQuestionCorrectCount()).isEqualTo(1L),
                () -> assertThat(skill.getTodayQuizQuestionCorrectCount()).isZero(),
                () -> assertThat(skill.getSubmitQuizQuestionCount()).isEqualTo(5L),
                () -> assertThat(skill.getSubmitTodayQuizQuestionCount()).isZero()
        );
    }

    @Test
    void 스킬에_오늘의_퀴즈_정답_결과를_적용한다() {
        // given
        Skill skill = new Skill(1L);

        // when
        skill.addCorrectTodayQuizQuestion(1L);

        // then
        assertAll(
                () -> assertThat(skill.getAccountId()).isEqualTo(1L),
                () -> assertThat(skill.getQuizQuestionCorrectCount()).isZero(),
                () -> assertThat(skill.getTodayQuizQuestionCorrectCount()).isEqualTo(1L),
                () -> assertThat(skill.getSubmitQuizQuestionCount()).isZero(),
                () -> assertThat(skill.getSubmitTodayQuizQuestionCount()).isEqualTo(1L)
        );
    }

    @Test
    void 생성된_퀴즈_문제_중_맞춘_정답의_비율을_계산한다() {
        // given
        Skill skill = new Skill(1L);

        skill.addCorrectQuizQuestion(1L);

        // when
        double actual = skill.calculateQuizQuestionCorrectPercent(5L);

        // then
        assertThat(actual).isEqualTo(20.0d);
    }

    @Test
    void 생성된_오늘의_퀴즈_문제_중_맞춘_정답의_비율을_계산한다() {
        // given
        Skill skill = new Skill(1L);

        skill.addCorrectTodayQuizQuestion(1L);

        // when
        double actual = skill.calculateTodayQuizQuestionCorrectPercent(5L);

        // then
        assertThat(actual).isEqualTo(20.0d);
    }

    @Test
    void 회원이_제출한_퀴즈_문제가_0개라면_정답의_비율을_0으로_계산한다() {
        // given
        Skill skill = new Skill(1L);

        // when
        double actual = skill.calculateQuizQuestionCorrectPercent(5L);

        // then
        assertThat(actual).isEqualTo(0.0d);
    }

    @Test
    void 회원이_제출한_오늘의_퀴즈_문제가_0개라면_정답의_비율을_0으로_계산한다() {
        // given
        Skill skill = new Skill(1L);

        // when
        double actual = skill.calculateTodayQuizQuestionCorrectPercent(5L);

        // then
        assertThat(actual).isEqualTo(0.0d);
    }

    @Test
    void 생성된_퀴즈_문제가_0개라면_정답의_비율을_0으로_계산한다() {
        // given
        Skill skill = new Skill(1L);

        // when
        double actual = skill.calculateQuizQuestionCorrectPercent(0L);

        // then
        assertThat(actual).isEqualTo(0.0d);
    }

    @Test
    void 생성된_오늘의_퀴즈_문제가_0개라면_정답의_비율을_0으로_계산한다() {
        // given
        Skill skill = new Skill(1L);

        // when
        double actual = skill.calculateTodayQuizQuestionCorrectPercent(0L);

        // then
        assertThat(actual).isEqualTo(0.0d);
    }
}
