package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.dnd.spaced.core.word.domain.dto.ViewCountStatisticsRankDto;
import java.time.LocalDateTime;
import java.util.List;

public interface WordViewCountStatisticsRepository {

    void save(Long wordId, LocalDateTime localDateTime);

    List<WordViewCountStatisticsDto> findAllBy(List<Long> ids, LocalDateTime localDateTime);

    List<ViewCountStatisticsRankDto> findAllBy(LocalDateTime localDateTime);

    void deleteAll(LocalDateTime localDateTime);
}
