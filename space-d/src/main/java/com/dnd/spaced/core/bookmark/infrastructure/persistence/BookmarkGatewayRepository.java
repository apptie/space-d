package com.dnd.spaced.core.bookmark.infrastructure.persistence;

import static com.dnd.spaced.core.bookmark.domain.QBookmark.bookmark;
import static com.dnd.spaced.core.word.domain.QWord.word;

import com.dnd.spaced.core.bookmark.domain.Bookmark;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public Optional<Bookmark> findBy(Long accountId, Long wordId) {
        BookmarkWithWord result = queryFactory.select(Projections.constructor(
                                                      BookmarkWithWord.class,
                                                      bookmark,
                                                      word.deleted
                                              ))
                                              .from(bookmark)
                                              .leftJoin(word).on(bookmark.wordId.eq(word.id))
                                              .where(
                                                      word.id.eq(wordId),
                                                      bookmark.accountId.eq(accountId)
                                              )
                                              .fetchOne();

        if (result == null || result.wordDeleted()) {
            return Optional.empty();
        }

        return Optional.of(result.bookmark);
    }

    @Override
    public boolean existsBy(Long accountId, Long wordId) {
        BookmarkWithWord result = queryFactory.select(Projections.constructor(
                                                      BookmarkWithWord.class,
                                                      bookmark,
                                                      word.deleted
                                              ))
                                              .from(bookmark)
                                              .leftJoin(word).on(bookmark.wordId.eq(word.id))
                                              .where(
                                                      word.id.eq(wordId),
                                                      bookmark.accountId.eq(accountId)
                                              )
                                              .fetchOne();

        return result != null && !result.wordDeleted;
    }

    @Override
    public void delete(Long accountId, Long wordId) {
        queryFactory.delete(bookmark)
                    .where(bookmark.accountId.eq(accountId), bookmark.wordId.eq(wordId))
                    .execute();
    }

    @Override
    public void deleteAllBy(Set<Long> wordId) {
        queryFactory.delete(bookmark)
                    .where(bookmark.wordId.in(wordId))
                    .execute();
    }

    @Override
    public List<Bookmark> findAllBy(Long accountId, Long lastBookmarkId, Pageable pageable) {
        return queryFactory.select(Projections.constructor(
                                   BookmarkWithWord.class,
                                   bookmark,
                                   word.deleted
                           ))
                           .from(bookmark)
                           .leftJoin(word).on(word.id.eq(bookmark.wordId))
                           .where(bookmark.accountId.eq(accountId), ltLastBookmarkId(lastBookmarkId))
                           .orderBy(bookmark.id.desc())
                           .limit(pageable.getPageSize())
                           .fetch()
                           .stream()
                           .filter(BookmarkWithWord::isNotWordDeleted)
                           .map(BookmarkWithWord::bookmark)
                           .toList();
    }

    private BooleanExpression ltLastBookmarkId(Long lastBookmarkId) {
        if (lastBookmarkId == null) {
            return null;
        }

        return bookmark.id.lt(lastBookmarkId);
    }

    public record BookmarkWithWord(Bookmark bookmark, boolean wordDeleted) {

        public boolean isNotWordDeleted() {
            return !wordDeleted();
        }
    }
}
