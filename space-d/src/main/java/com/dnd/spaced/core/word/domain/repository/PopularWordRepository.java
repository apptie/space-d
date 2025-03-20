package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.dto.PopularWord;
import java.time.LocalDateTime;
import java.util.List;

public interface PopularWordRepository {

    boolean existsBy(Long wordId, LocalDateTime localDateTime);

    List<PopularWord> findAllBy(LocalDateTime localDateTime);

    void deleteAll(LocalDateTime localDateTime);

    void saveAll(List<PopularWord> popularWords, LocalDateTime localDateTime);
}
