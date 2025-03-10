package com.dnd.spaced.core.skill.application.dto;

import com.dnd.spaced.core.skill.application.dto.response.SkillResponse;
import com.dnd.spaced.core.skill.domain.Skill;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkillApplicationMapper {

    public static SkillResponse toDto(
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

    public static SkillResponse toDto(Long accountId) {
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
