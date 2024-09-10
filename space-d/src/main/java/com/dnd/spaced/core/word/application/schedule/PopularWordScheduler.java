package com.dnd.spaced.core.word.application.schedule;

import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.WordViewCountStatisticsRepository;
import com.dnd.spaced.core.word.domain.repository.dto.PopularWordInfo;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.dnd.spaced.core.word.domain.repository.dto.response.ViewCountStatisticsRankDto;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PopularWordScheduler {

    private final Clock clock;
    private final WordRepository wordRepository;
    private final PopularWordRepository popularWordRepository;
    private final WordViewCountStatisticsRepository wordViewCountStatisticsRepository;

    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void schedule() {
        LocalDateTime today = LocalDateTime.now(clock);
        LocalDateTime yesterday = today.minusDays(1L);
        LocalDateTime beforeYesterday = yesterday.minusDays(1L);

        updatePopularWordViewCount(yesterday);
        updatePopularWord(today);
        clearViewCountMetadata(yesterday, beforeYesterday);
    }

    private void updatePopularWord(LocalDateTime today) {
        List<ViewCountStatisticsRankDto> ranking = wordViewCountStatisticsRepository.findByRanking(today);
        Long[] ids = ranking.stream()
                              .map(ViewCountStatisticsRankDto::wordId)
                              .toArray(Long[]::new);
        List<String> names = wordRepository.findNameAllBy(ids);
        List<PopularWordInfo> popularWordInfos = calculatePopularWordInfo(ranking, names);

        popularWordRepository.saveAll(popularWordInfos, today);
    }

    private void updatePopularWordViewCount(LocalDateTime yesterday) {
        List<Long> ids = popularWordRepository.findAllBy(yesterday)
                                              .stream()
                                              .map(PopularWordInfo::wordId)
                                              .toList();
        List<WordViewCountStatisticsDto> dtos = wordViewCountStatisticsRepository.findAllBy(ids, yesterday);

        wordRepository.updateViewCount(dtos);
    }

    private List<PopularWordInfo> calculatePopularWordInfo(List<ViewCountStatisticsRankDto> ranking, List<String> names) {
        List<PopularWordInfo> popularWordInfos = new ArrayList<>();

        for (int i = 0; i < ranking.size(); i++) {
            ViewCountStatisticsRankDto targetRankDto = ranking.get(i);
            String targetName = names.get(i);

            popularWordInfos.add(new PopularWordInfo(targetRankDto.rank(), targetRankDto.wordId(), targetName));
        }

        return popularWordInfos;
    }

    private void clearViewCountMetadata(LocalDateTime yesterday, LocalDateTime beforeYesterday) {
        popularWordRepository.deleteAll(yesterday);
        wordViewCountStatisticsRepository.deleteAll(beforeYesterday);
    }
}
