package com.dnd.spaced.core.bookmark.application;

import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.exception.AlreadyExistsBookmarkException;
import com.dnd.spaced.core.bookmark.application.exception.BookmarkInterruptedException;
import com.dnd.spaced.core.bookmark.application.exception.BookmarkLockException;
import com.dnd.spaced.core.bookmark.application.exception.WordNotFoundException;
import com.dnd.spaced.core.bookmark.domain.Bookmark;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
class CreateBookmarkService {

    private static final int LOCK_WAIT_TIME = 1;
    private static final int LOCK_LEASE_TIME = 1;
    private static final String LOCK_SEPARATOR = ":";
    private static final String LOCK_PREFIX = "bookmark" + LOCK_SEPARATOR + "create" + LOCK_SEPARATOR;

    private final RedissonClient redissonClient;
    private final WordRepository wordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TransactionTemplate transactionTemplate;
    
    public void createBookmark(Long accountId, CreateBookmarkRequest request) {
        validateWordId(request);
        executeBookmarkCreationWithTransaction(accountId, request);
    }

    private void executeBookmarkCreationWithTransaction(Long accountId, CreateBookmarkRequest request) {
        RLock lock = redissonClient.getLock(calculateLockName(accountId, request));

        if (!tryLock(lock)) {
            throw new BookmarkLockException("북마크 생성 중 락 획득 실패");
        }

        try {
            transactionTemplate.executeWithoutResult(action -> doBookmarkCreation(accountId, request));
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private boolean tryLock(RLock lock) {
        try {
            return lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);
        } catch (RedisException e) {
            if (e.getCause() instanceof InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                throw new BookmarkInterruptedException("북마크 생성 중 인터럽트 발생", interruptedException);
            }
            throw new BookmarkLockException("북마크 생성 중 락 획득 실패", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BookmarkInterruptedException("북마크 생성 중 인터럽트 발생", e);
        }
    }

    private void doBookmarkCreation(Long accountId, CreateBookmarkRequest request) {
        validateExistsBookmark(accountId, request);
        persistBookmark(accountId, request);
    }

    private String calculateLockName(Long accountId, CreateBookmarkRequest request) {
        return LOCK_PREFIX + accountId + LOCK_SEPARATOR + request.wordId();
    }

    private void validateExistsBookmark(Long accountId, CreateBookmarkRequest request) {
        if (bookmarkRepository.existsBy(accountId, request.wordId())) {
            throw new AlreadyExistsBookmarkException("이미 북마크에 추가된 용어입니다.");
        }
    }

    private void persistBookmark(Long accountId, CreateBookmarkRequest request) {
        Bookmark bookmark = new Bookmark(accountId, request.wordId());

        bookmarkRepository.save(bookmark);
    }

    private void validateWordId(CreateBookmarkRequest request) {
        if (isMissingWord(request.wordId())) {
            throw new WordNotFoundException("지정한 식별자의 용어를 찾지 못했습니다.");
        }
    }

    private boolean isMissingWord(Long wordId) {
        return !wordRepository.existsBy(wordId);
    }
}
