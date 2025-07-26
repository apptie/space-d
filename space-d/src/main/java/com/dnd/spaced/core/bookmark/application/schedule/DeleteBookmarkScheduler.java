package com.dnd.spaced.core.bookmark.application.schedule;

import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import com.dnd.spaced.core.word.application.repository.DeletedWordIdRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteBookmarkScheduler {

    private final Clock clock;
    private final RetryTemplate repositoryRetryTemplate;
    private final BookmarkRepository bookmarkRepository;
    private final DeletedWordIdRepository deletedWordIdRepository;

    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void schedule() {
        LocalDateTime targetTime = LocalDateTime.now(clock);

        try {
            repositoryRetryTemplate.execute(
                    retryContext -> {
                        Set<Long> deletedWordIds = deletedWordIdRepository.findAllBy(targetTime);

                        bookmarkRepository.deleteAllBy(deletedWordIds);
                        deletedWordIdRepository.deleteAllBy(targetTime);
                        return null;
                    },
                    retryContext -> {
                        log.error(
                                "[DeleteBookmarkScheduler.schedule] {} 용어 삭제로 인한 북마크 삭제 이벤트 처리 실패",
                                LocalDateTime.now(clock),
                                retryContext.getLastThrowable()
                        );

                        return null;
                    }
            );
        } catch (Throwable e) {
            log.error("[DeleteBookmarkScheduler.schedule] retry 실패", e);
        }
    }
}
