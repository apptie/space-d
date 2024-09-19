package com.dnd.spaced.core.like.infrastructure;

import com.dnd.spaced.core.like.domain.Like;
import org.springframework.data.repository.CrudRepository;

interface LikeCrudRepository extends CrudRepository<Like, Long> {
}
