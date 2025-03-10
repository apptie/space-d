package com.dnd.spaced.core.word.application.dto.response;

import java.util.List;

public record WordCollectionResponse(List<WordResponse> words, String lastWordName) {
}
