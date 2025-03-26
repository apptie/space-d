package com.dnd.spaced.core.skill.application.event.listener;

import com.dnd.spaced.core.skill.application.event.dto.InitializedAccountEvent;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitSkillListener {

    private final SkillRepository skillRepository;

    @EventListener
    public void listen(InitializedAccountEvent event) {
        Skill skill = new Skill(event.accountId());

        skillRepository.save(skill);
    }
}
