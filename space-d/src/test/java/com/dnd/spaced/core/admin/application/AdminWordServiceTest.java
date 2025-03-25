package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.exception.PronunciationDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.UnexpectedUpdateWordExampleCountException;
import com.dnd.spaced.core.admin.application.exception.WordExampleDeletionNotAllowedException;
import java.util.List;
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
class AdminWordServiceTest {

    @Autowired
    AdminWordService adminWordService;

    @Test
    @Sql(scripts = {"classpath:sql/cleanup.sql", "classpath:sql/admin/word/word_metadata.sql"})
    void 용어를_추가한다() {
        // given
        List<CreatePronunciationRequest> createPronunciationRequests = List.of(
                new CreatePronunciationRequest("어써라이제이션", "한글 발음")
        );
        List<String> examples = List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.");
        CreateWordRequest request = new CreateWordRequest(
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                createPronunciationRequests,
                examples
        );

        // when
        Long actual = adminWordService.createWord(request);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_예문을_변경한다() {
        // when & then
        assertDoesNotThrow(() -> adminWordService.updateWordExample(
                        1L,
                "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다.")
        );
    }

    @Test
    void 없는_용어_예문_ID라면_용어_예문을_변경할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> adminWordService.updateWordExample(
                        -999L,
                        "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다."
                )
        ).isInstanceOf(UnexpectedUpdateWordExampleCountException.class)
         .hasMessage("용어 예문이 정상적으로 변경되지 않았습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_예문을_삭제한다() {
        // when & then
        assertDoesNotThrow(
                () -> adminWordService.deleteWordExample(1L, 1L)
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_예문의_개수가_최소치라면_용어_예문을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> adminWordService.deleteWordExample(2L, 3L)
        ).isInstanceOf(WordExampleDeletionNotAllowedException.class)
         .hasMessage("해당 용어의 예문 개수가 최소치입니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_발음_정보를_삭제한다() {
        // when & then
        assertDoesNotThrow(
                () -> adminWordService.deletePronunciation(1L, 1L)
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/cleanup.sql",
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_발음_정보의_개수가_최소치라면_용어_발음_정보를_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> adminWordService.deletePronunciation(2L, 1L)
        ).isInstanceOf(PronunciationDeletionNotAllowedException.class)
         .hasMessage("해당 용어의 발음 정보 개수가 최소치입니다.");
    }
}
