package com.dnd.spaced.core.word.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.application.dto.request.SearchWordRequest;
import com.dnd.spaced.core.word.domain.dto.WordView;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SearchWordViewServiceTest {

    @Autowired
    SearchWordViewService wordService;

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 용어를_검색한다() {
        // given
        SearchWordRequest request = new SearchWordRequest(
                "Authorization",
                null,
                null,
                null,
                null
        );

        // when
        List<WordView> actual = wordService.searchWord(request, Pageable.ofSize(10));

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).name()).isEqualTo("Authorization")
        );
    }
}
