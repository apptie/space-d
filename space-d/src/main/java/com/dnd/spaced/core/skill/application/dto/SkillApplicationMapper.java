package com.dnd.spaced.core.skill.application.dto;

import com.dnd.spaced.core.skill.application.dto.response.SkillResponse;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.global.mapper.Mapper;

@Mapper
public class SkillApplicationMapper {

    public SkillResponse toDefaultDto(
            Skill skill,
            double totalQuizQuestionCorrectPercent,
            double totalTodayQuizQuestionCorrectPercent
    ) {
        return new SkillResponse(
                skill.getAccountId(),
                skill.getSubmitQuizQuestionCount(),
                skill.getQuizQuestionCorrectCount(),
                skill.getSubmitTodayQuizQuestionCount(),
                skill.getTodayQuizQuestionCorrectCount(),
                totalQuizQuestionCorrectPercent,
                totalTodayQuizQuestionCorrectPercent
        );
    }

    public SkillResponse toDefaultDto(Long accountId) {
        return new SkillResponse(
                accountId,
                0L,
                0L,
                0L,
                0L,
                0.0d,
                0.0d
        );
    }
}
