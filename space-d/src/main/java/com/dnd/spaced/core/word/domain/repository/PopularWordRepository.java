package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.repository.dto.PopularWordInfo;
import java.time.LocalDateTime;
import java.util.List;

public interface PopularWordRepository {

    boolean existsBy(Long wordId, LocalDateTime localDateTime);

    List<PopularWordInfo> findAllBy(LocalDateTime localDateTime);

    void deleteAll(LocalDateTime localDateTime);

    void saveAll(List<PopularWordInfo> popularWordInfos, LocalDateTime localDateTime);
}
