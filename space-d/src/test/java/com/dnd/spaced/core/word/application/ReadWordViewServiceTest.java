package com.dnd.spaced.core.word.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.application.dto.request.ReadAllWordRequest;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.enums.Category;
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
class ReadWordViewServiceTest {

    private static final Long NOT_FOUND_WORD_ID = -999L;

    @Autowired
    ReadWordViewService wordService;

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 용어를_조회한다() {
        // when
        WordView actual = wordService.readWord(1L);

        // then
        assertAll(
                () -> assertThat(actual.name()).isEqualTo("Authorization"),
                () -> assertThat(actual.category()).isEqualTo(Category.DEVELOP),
                () -> assertThat(actual.wordMeaning().getMeaning()).isEqualTo("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
        );
    }

    @Test
    void 용어_ID로_용어를_찾지_못하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> wordService.readWord(NOT_FOUND_WORD_ID))
                .isInstanceOf(WordNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 용어를 찾을 수 없습니다.");
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 용어_목록을_조회한다() {
        // given
        ReadAllWordRequest request = new ReadAllWordRequest(
                null,
                null,
                null
        );

        // when
        List<WordView> actual = wordService.readWords(request, Pageable.ofSize(10));

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).name()).isEqualTo("Authorization")
        );
    }
}
