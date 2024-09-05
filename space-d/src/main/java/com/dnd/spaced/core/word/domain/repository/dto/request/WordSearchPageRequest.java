package com.dnd.spaced.core.word.domain.repository.dto.request;

import org.springframework.data.domain.Pageable;

public record WordSearchPageRequest(Pageable pageable, String lastWordName) {
}
