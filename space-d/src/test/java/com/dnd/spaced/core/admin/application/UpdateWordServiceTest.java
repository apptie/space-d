package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.admin.application.exception.WordExampleNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UpdateWordServiceTest {

    @Autowired
    UpdateWordService updateWordService;
    
    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_예문을_변경한다() {
        // when & then
        assertDoesNotThrow(() -> updateWordService.updateWordExample(
                1L,
                "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다.")
        );
    }

    @Test
    void 잘못된_용어_예문_ID라면_용어_예문을_변경할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> updateWordService.updateWordExample(
                        -999L,
                        "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다."
                )
        ).isInstanceOf(WordExampleNotFoundException.class)
         .hasMessage("지정한 용어 예문을 찾을 수 없습니다.");
    }
}
