package com.dnd.spaced.core.word.presentation;

import com.dnd.spaced.core.word.application.WordService;
import com.dnd.spaced.core.word.application.dto.request.SearchConditionDto;
import com.dnd.spaced.core.word.application.dto.response.ReadAllWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadWordDto;
import com.dnd.spaced.core.word.application.dto.response.SearchedWordDto;
import com.dnd.spaced.core.word.presentation.dto.request.ReadWordAllRequest;
import com.dnd.spaced.core.word.presentation.dto.request.SearchWordRequest;
import com.dnd.spaced.core.word.presentation.dto.response.ReadWordAllResponse;
import com.dnd.spaced.core.word.presentation.dto.response.ReadWordResponse;
import com.dnd.spaced.core.word.presentation.dto.response.SearchedWordResponse;
import com.dnd.spaced.global.resolver.word.CommonWordPageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping("/{id}")
    public ResponseEntity<ReadWordResponse> read(@PathVariable Long id) {
        ReadWordDto result = wordService.read(id);

        return ResponseEntity.ok(ReadWordResponse.from(result));
    }

    @GetMapping
    public ResponseEntity<ReadWordAllResponse> readAllBy(
            ReadWordAllRequest request,
            @CommonWordPageable Pageable pageable
    ) {
        List<ReadAllWordDto> result = wordService.readAllBy(
                request.categoryName(),
                request.lastWordName(),
                pageable
        );

        return ResponseEntity.ok(ReadWordAllResponse.from(result));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchedWordResponse> search(
            SearchWordRequest request,
            @CommonWordPageable Pageable pageable
    ) {
        SearchConditionDto searchConditionDto = new SearchConditionDto(
                request.name(),
                request.categoryName(),
                request.pronunciation(),
                pageable,
                request.lastWordName()
        );
        List<SearchedWordDto> result = wordService.search(searchConditionDto);

        return ResponseEntity.ok(SearchedWordResponse.from(result));
    }
}
