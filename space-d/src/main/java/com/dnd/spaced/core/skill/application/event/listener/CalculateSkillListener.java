package com.dnd.spaced.core.skill.application.event.listener;

import com.dnd.spaced.core.skill.application.event.dto.GradedQuizEvent;
import com.dnd.spaced.core.skill.application.event.dto.GradedTodayQuizEvent;
import com.dnd.spaced.core.skill.application.event.listener.exception.SkillNotFoundException;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CalculateSkillListener {

    private static final long TODAY_QUIZ_CORRECT_COUNT = 1L;

    private final SkillRepository skillRepository;

    @Async("asyncCalculateSkillExecutor")
    @EventListener
    @Transactional
    public void listen(GradedQuizEvent event) {
        Skill skill = findSkill(event.accountId());

        skill.addCorrectQuizQuestion(event.correctCount());
    }

    @Async("asyncCalculateSkillExecutor")
    @EventListener
    @Transactional
    public void listen(GradedTodayQuizEvent event) {
        if (!event.corrected()) {
            return;
        }

        Skill skill = findSkill(event.accountId());

        skill.addCorrectTodayQuizQuestion(TODAY_QUIZ_CORRECT_COUNT);
    }

    private Skill findSkill(Long accountId) {
        return skillRepository.findBy(accountId)
                              .orElseThrow(() -> new SkillNotFoundException("지정한 회원의 스킬을 찾지 못했습니다."));
    }
}
