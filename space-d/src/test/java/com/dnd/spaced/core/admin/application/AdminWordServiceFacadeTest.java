package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.event.dto.DeletedWordEvent;
import com.dnd.spaced.core.admin.application.exception.PronunciationDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.PronunciationNotFoundException;
import com.dnd.spaced.core.admin.application.exception.WordExampleDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.WordExampleNotFoundException;
import com.dnd.spaced.core.word.application.event.dto.PersistedWordEvent;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.exception.InvalidWordExampleContentException;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminWordServiceFacadeTest {

    @Autowired
    AdminWordServiceFacade adminWordServiceFacade;

    @Autowired
    ApplicationEvents events;

    @Test
    @Sql("classpath:sql/admin/word/word_metadata.sql")
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
        Long actual = adminWordServiceFacade.createWord(request);

        // then
        assertAll(
                () -> assertThat(actual).isPositive(),
                () -> assertThat(events.stream(PersistedWordEvent.class).count()).isOne()
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_예문을_변경한다() {
        // when & then
        assertDoesNotThrow(() -> adminWordServiceFacade.updateWordExample(
                        1L,
                "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다.")
        );
    }

    @Test
    void 잘못된_용어_예문_ID라면_용어_예문을_변경할_수_없다() {
        // when & then
        assertThatThrownBy(
                () -> adminWordServiceFacade.updateWordExample(
                        -999L,
                        "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다."
                )
        ).isInstanceOf(WordExampleNotFoundException.class)
         .hasMessage("지정한 용어 예문을 찾을 수 없습니다.");
    }

    @ParameterizedTest(name = "변경하고자 하는 예문이 {0}일 때 예문을 변경할 수 없다")
    @NullAndEmptySource
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 유효하지_않은_길이의_예문으로_용어_예문을_변경할_수_없다(String invalidContent) {
        // when & then
        assertThatThrownBy(
                () -> adminWordServiceFacade.updateWordExample(
                        1L,
                        invalidContent
                )
        ).isInstanceOf(InvalidWordExampleContentException.class)
         .hasMessage("예문의 길이는 최소 1글자 이상, 최대 150글자 이하여야 합니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 용어_예문을_삭제한다() {
        // when & then
        assertDoesNotThrow(
                () -> adminWordServiceFacade.deleteWordExample(1L, 1L)
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 잘못된_용어_예문_ID로_용어_예문을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> adminWordServiceFacade.deleteWordExample(1L, -999L))
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
                () -> adminWordServiceFacade.deleteWordExample(2L, 3L)
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
        assertThatThrownBy(() -> adminWordServiceFacade.deletePronunciation(1L, -999L))
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
                () -> adminWordServiceFacade.deletePronunciation(1L, 1L)
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
                () -> adminWordServiceFacade.deletePronunciation(2L, 1L)
        ).isInstanceOf(PronunciationDeletionNotAllowedException.class)
         .hasMessage("해당 용어의 발음 정보 개수가 최소치입니다.");
    }

    @Test
    void 유효하지_않은_용어_ID로_용어를_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> adminWordServiceFacade.deleteWord(-999L))
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
        assertDoesNotThrow(() -> adminWordServiceFacade.deleteWord(1L));
        assertThat(events.stream(DeletedWordEvent.class).count()).isOne();
    }
}
