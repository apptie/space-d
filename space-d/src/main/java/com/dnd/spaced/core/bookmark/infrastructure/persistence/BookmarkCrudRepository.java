package com.dnd.spaced.core.bookmark.infrastructure.persistence;

import com.dnd.spaced.core.bookmark.domain.Bookmark;
import org.springframework.data.repository.CrudRepository;

interface BookmarkCrudRepository extends CrudRepository<Bookmark, Long> {
}
