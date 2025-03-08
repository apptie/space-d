package com.dnd.spaced.core.bookmark.infrastructure.persistence;

import static com.dnd.spaced.core.bookmark.domain.QBookmark.bookmark;

import com.dnd.spaced.core.bookmark.domain.Bookmark;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkGatewayRepository implements BookmarkRepository {

    private final JPAQueryFactory queryFactory;
    private final BookmarkCrudRepository bookmarkCrudRepository;

    @Override
    public void save(Bookmark bookmark) {
        bookmarkCrudRepository.save(bookmark);
    }

    @Override
    public Optional<Bookmark> findBy(Long bookmarkId) {
        return bookmarkCrudRepository.findById(bookmarkId);
    }

    @Override
    public void delete(Bookmark bookmark) {
        bookmarkCrudRepository.delete(bookmark);
    }

    @Override
    public List<Bookmark> findAllBy(Long accountId, Long lastBookmarkId, Pageable pageable) {
        return queryFactory.selectFrom(bookmark)
                           .where(bookmark.accountId.eq(accountId), ltLastBookmarkId(lastBookmarkId))
                           .orderBy(bookmark.id.desc())
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    private BooleanExpression ltLastBookmarkId(Long lastBookmarkId) {
        if (lastBookmarkId == null) {
            return null;
        }

        return bookmark.id.lt(lastBookmarkId);
    }
}
