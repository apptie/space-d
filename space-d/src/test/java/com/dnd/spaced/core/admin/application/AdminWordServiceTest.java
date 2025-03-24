package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.exception.PronunciationDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.UnexpectedUpdateWordExampleCountException;
import com.dnd.spaced.core.admin.application.exception.WordExampleDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.admin.application.helper.WithWordMetadataTestHelper;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@CleanUpDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminWordServiceTest {

    @Autowired
    AdminWordService adminWordService;

    @Autowired
    WordRepository wordRepository;

    @Test
    void 용어_메타데이터가_초기화되지_않았다면_용어를_추가할_수_없다() {
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
        assertThatThrownBy(() -> adminWordService.createWord(request))
                .isInstanceOf(WordMetadataNotFoundException.class)
                .hasMessage("용어 메타데이터가 정상적으로 설정되지 않았습니다.");
    }

    @Nested
    class WithMetadataTest extends WithWordMetadataTestHelper {

        @Test
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
            assertAll(
                    () -> assertThat(actual).isPositive(),
                    () -> assertThat(wordMetadata.getTotalWordCount()).isOne(),
                    () -> assertThat(wordMetadata.getBusinessWordCount()).isZero(),
                    () -> assertThat(wordMetadata.getDevelopWordCount()).isOne(),
                    () -> assertThat(wordMetadata.getDesignWordCount()).isZero()
            );
        }

        @Test
        void 용어_예문을_변경한다() {
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

            Long wordId = adminWordService.createWord(request);
            Word word = wordRepository.findBy(wordId)
                                      .get();

            // when & then
            assertDoesNotThrow(() -> adminWordService.updateWordExample(
                    word.getWordExamples().get(0).getId(),
                    "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다.")
            );
        }

        @Test
        void 없는_용어_예문_ID라면_용어_예문을_변경할_수_없다() {
            // when & then
            assertThatThrownBy(
                    () -> adminWordService.updateWordExample(
                            1L,
                            "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다."
                    )
            ).isInstanceOf(UnexpectedUpdateWordExampleCountException.class)
             .hasMessage("용어 예문이 정상적으로 변경되지 않았습니다.");
        }

        @Test
        void 용어_예문을_삭제한다() {
            // given
            List<CreatePronunciationRequest> createPronunciationRequests = List.of(new CreatePronunciationRequest("어써라이제이션", "한글 발음"));
            List<String> examples = List.of(
                    "게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.",
                    "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다."
            );
            CreateWordRequest request = new CreateWordRequest(
                    "Authorization",
                    "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                    "개발",
                    createPronunciationRequests,
                    examples
            );

            Long wordId = adminWordService.createWord(request);
            Word word = wordRepository.findBy(wordId)
                                      .get();

            // when & then
            assertDoesNotThrow(
                    () -> adminWordService.deleteWordExample(wordId, word.getWordExamples().get(0).getId())
            );
        }

        @Test
        void 용어_예문의_개수가_최소치라면_용어_예문을_삭제할_수_없다() {
            // given
            List<CreatePronunciationRequest> createPronunciationRequests = List.of(new CreatePronunciationRequest("어써라이제이션", "한글 발음"));
            List<String> examples = List.of(
                    "게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다."
            );
            CreateWordRequest request = new CreateWordRequest(
                    "Authorization",
                    "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                    "개발",
                    createPronunciationRequests,
                    examples
            );

            Long wordId = adminWordService.createWord(request);
            Word word = wordRepository.findBy(wordId)
                                      .get();

            // when & then
            assertThatThrownBy(
                    () -> adminWordService.deleteWordExample(wordId, word.getWordExamples().get(0).getId())
            ).isInstanceOf(WordExampleDeletionNotAllowedException.class)
             .hasMessage("해당 용어의 예문 개수가 최소치입니다.");
        }

        @Test
        void 용어_발음_정보를_삭제한다() {
            // given
            List<CreatePronunciationRequest> createPronunciationRequests = List.of(
                    new CreatePronunciationRequest("어써라이제이션", "한글 발음"),
                    new CreatePronunciationRequest("오써러제이션", "한글 발음")
            );
            List<String> examples = List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.");
            CreateWordRequest request = new CreateWordRequest(
                    "Authorization",
                    "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                    "개발",
                    createPronunciationRequests,
                    examples
            );

            Long wordId = adminWordService.createWord(request);
            Word word = wordRepository.findBy(wordId)
                                      .get();

            // when & then
            assertDoesNotThrow(
                    () -> adminWordService.deletePronunciation(wordId, word.getPronunciations().get(0).getId())
            );
        }

        @Test
        void 용어_발음_정보의_개수가_최소치라면_용어_발음_정보를_삭제할_수_없다() {
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

            Long wordId = adminWordService.createWord(request);
            Word word = wordRepository.findBy(wordId)
                                      .get();

            // when & then
            assertThatThrownBy(
                    () -> adminWordService.deletePronunciation(wordId, word.getPronunciations().get(0).getId())
            ).isInstanceOf(PronunciationDeletionNotAllowedException.class)
             .hasMessage("해당 용어의 발음 정보 개수가 최소치입니다.");
        }
    }
}
