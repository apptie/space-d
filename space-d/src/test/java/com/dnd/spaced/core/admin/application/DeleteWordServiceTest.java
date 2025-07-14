package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.admin.application.exception.PronunciationDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.PronunciationNotFoundException;
import com.dnd.spaced.core.admin.application.exception.WordExampleDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.WordExampleNotFoundException;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
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
class DeleteWordServiceTest {

    @Autowired
    DeleteWordService deleteWordService;

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_예문을_삭제한다() {
        // when & then
        assertDoesNotThrow(
                () -> deleteWordService.deleteWordExample(1L, 1L)
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 잘못된_용어_예문_ID로_용어_예문을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> deleteWordService.deleteWordExample(1L, -999L))
                .isInstanceOf(WordExampleNotFoundException.class)
                .hasMessage("지정한 용어 예문을 찾을 수 없습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_예문의_개수가_최소치라면_용어_예문을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> deleteWordService.deleteWordExample(2L, 3L)
        ).isInstanceOf(WordExampleDeletionNotAllowedException.class)
         .hasMessage("해당 용어의 예문 개수가 최소치입니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 잘못된_용어_발음_ID로_용어_발음을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> deleteWordService.deletePronunciation(1L, -999L))
                .isInstanceOf(PronunciationNotFoundException.class)
                .hasMessage("지정한 발음을 찾지 못했습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_발음_정보를_삭제한다() {
        // when & then
        assertDoesNotThrow(
                () -> deleteWordService.deletePronunciation(1L, 1L)
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_발음_정보의_개수가_최소치라면_용어_발음_정보를_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> deleteWordService.deletePronunciation(2L, 1L)
        ).isInstanceOf(PronunciationDeletionNotAllowedException.class)
         .hasMessage("해당 용어의 발음 정보 개수가 최소치입니다.");
    }

    @Test
    void 유효하지_않은_용어_ID로_용어를_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> deleteWordService.deleteWord(-999L))
                .isInstanceOf(WordNotFoundException.class)
                .hasMessage("지정한 용어를 찾을 수 없습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어를_삭제한다() {
        // when & then
        assertDoesNotThrow(() -> deleteWordService.deleteWord(1L));
    }
}
