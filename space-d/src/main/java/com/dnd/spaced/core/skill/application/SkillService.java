package com.dnd.spaced.core.skill.application;

import com.dnd.spaced.core.quiz.domain.QuizMetadata;
import com.dnd.spaced.core.quiz.domain.repository.QuizMetadataRepository;
import com.dnd.spaced.core.skill.application.dto.response.SkillResponse;
import com.dnd.spaced.core.skill.application.exception.QuizMetadataNotFoundException;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillService {

    private static final Long DEFAULT_WORD_METADATA_ID = 1L;

    private final SkillRepository skillRepository;
    private final QuizMetadataRepository quizMetadataRepository;

    public SkillResponse findBy(Long accountId) {
        QuizMetadata quizMetadata = quizMetadataRepository.findBy(DEFAULT_WORD_METADATA_ID)
                                                          .orElseThrow(
                                                                  () -> new QuizMetadataNotFoundException(
                                                                          "퀴즈 메타데이터가 정상적으로 설정되지 않았습니다."
                                                                  )
                                                          );

        return skillRepository.findBy(accountId)
                              .map(skill -> handleFoundSkill(skill, quizMetadata))
                              .orElseGet(() -> SkillApplicationMapper.toDto(accountId));
    }

    private SkillResponse handleFoundSkill(Skill skill, QuizMetadata quizMetadata) {
        double totalQuizQuestionCorrectPercent = skill.calculateQuizQuestionCorrectPercent(
                quizMetadata.getTotalQuizQuestionCount()
        );
        double totalTodayQuizQuestionCorrectPercent = skill.calculateTodayQuizQuestionCorrectPercent(
                quizMetadata.getTotalTodayQuizQuestionCount()
        );

        return SkillApplicationMapper.toDto(
                skill,
                totalQuizQuestionCorrectPercent,
                totalTodayQuizQuestionCorrectPercent
        );
    }
}
