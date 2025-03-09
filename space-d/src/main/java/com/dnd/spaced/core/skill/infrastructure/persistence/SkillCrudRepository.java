package com.dnd.spaced.core.skill.infrastructure.persistence;

import com.dnd.spaced.core.skill.domain.Skill;
import org.springframework.data.repository.CrudRepository;

interface SkillCrudRepository extends CrudRepository<Skill, Long> {
}
