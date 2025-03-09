package com.dnd.spaced.core.skill.infrastructure.persistence;

import static com.dnd.spaced.core.skill.domain.QSkill.skill;

import com.dnd.spaced.core.skill.domain.Skill;
import com.dnd.spaced.core.skill.domain.repository.SkillRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SkillGatewayRepository implements SkillRepository {

    private final JPAQueryFactory queryFactory;
    private final SkillCrudRepository skillCrudRepository;

    @Override
    public void save(Skill skill) {
        skillCrudRepository.save(skill);
    }

    @Override
    public Optional<Skill> findBy(Long accountId) {
        Skill result = queryFactory.selectFrom(skill)
                                   .where(skill.accountId.eq(accountId))
                                   .fetchOne();

        return Optional.ofNullable(result);
    }
}
