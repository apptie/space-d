package com.dnd.spaced.core.skill.domain.repository;

import com.dnd.spaced.core.skill.domain.Skill;
import java.util.Optional;

public interface SkillRepository {

    void save(Skill skill);

    Optional<Skill> findBy(Long accountId);
}
