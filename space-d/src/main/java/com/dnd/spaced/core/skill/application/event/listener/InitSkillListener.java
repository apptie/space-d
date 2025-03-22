package com.dnd.spaced.core.skill.application.event.listener;

import com.dnd.spaced.core.skill.application.event.dto.AccountInitializedEvent;
import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitSkillListener {

    private final SkillRepository skillRepository;

    @EventListener
    @Transactional
    public void listen(AccountInitializedEvent event) {
        Skill skill = new Skill(event.accountId());

        skillRepository.save(skill);
    }
}
