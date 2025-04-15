package com.dnd.spaced.core.word.application;

import java.time.LocalDateTime;
import java.util.Set;

public interface DeletedWordIdRepository {

    void save(Long wordId, LocalDateTime targetTime);

    Set<Long> findAllBy(LocalDateTime targetTime);

    void deleteAllBy(LocalDateTime targetTime);
}
