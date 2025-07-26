package com.dnd.spaced.core.word.application;

import com.dnd.spaced.core.word.application.dto.request.ReadAllWordRequest;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.WordViewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReadWordViewService {

    private static final Category NO_CATEGORY_FILTER = null;
    private static final Category NO_LAST_CATEGORY_FILTER = null;

    private final WordViewRepository wordViewRepository;

    public WordView readWord(Long wordId) {
        return wordViewRepository.findBy(wordId)
                                 .orElseThrow(
                                         () -> new WordNotFoundException(
                                                 "지정한 ID에 해당하는 용어를 찾을 수 없습니다."
                                         )
                                 );
    }

    public List<WordView> readWords(ReadAllWordRequest request, Pageable pageable) {
        Category category = Category.findBy(request.categoryName())
                                    .orElse(NO_CATEGORY_FILTER);
        Category lastCategory = Category.findBy(request.lastCategoryName())
                                        .orElse(NO_LAST_CATEGORY_FILTER);
        return wordViewRepository.findAllBy(category, request.lastWordName(), lastCategory, pageable);
    }
}
