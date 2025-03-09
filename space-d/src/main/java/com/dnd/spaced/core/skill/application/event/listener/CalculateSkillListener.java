package com.dnd.spaced.core.skill.application.event.listener;

import com.dnd.spaced.core.skill.application.event.dto.GradedQuizEvent;
import com.dnd.spaced.core.skill.application.event.dto.GradedTodayQuizEvent;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CalculateSkillListener {

    private final SkillRepository skillRepository;

    @EventListener
    @Transactional
    public void listen(GradedQuizEvent event) {
        skillRepository.findBy(event.accountId())
                       .ifPresentOrElse(
                               skill -> skill.addCorrectQuizQuestion(event.correctCount()),
                               () -> {
                                   Skill skill = new Skill(event.accountId());

                                   skill.addCorrectQuizQuestion(event.correctCount());
                                   skillRepository.save(skill);
                               }
                       );
    }

    @EventListener
    @Transactional
    public void listen(GradedTodayQuizEvent event) {
        skillRepository.findBy(event.accountId())
                                     .ifPresentOrElse(
                                             skill -> skill.addCorrectQuizQuestion(event.correctCount()),
                                             () -> {
                                                 Skill skill = new Skill(event.accountId());

                                                 skill.addCorrectTodayQuizQuestion(event.correctCount());
                                                 skillRepository.save(skill);
                                             }
                                     );
    }
}
