package com.dnd.spaced.core.bookmark.domain.repository;

import com.dnd.spaced.core.bookmark.domain.Bookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface BookmarkRepository {

    void save(Bookmark bookmark);

    Optional<Bookmark> findBy(Long accountId, Long wordId);

    boolean existsBy(Long accountId, Long wordId);

    List<Bookmark> findAllBy(Long accountId, Long lastBookmarkId, Pageable pageable);

    void delete(Long accountId, Long wordId);
}
