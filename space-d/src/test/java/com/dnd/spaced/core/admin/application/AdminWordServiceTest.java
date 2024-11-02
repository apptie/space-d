package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto.PronunciationInfoDto;
import com.dnd.spaced.core.admin.application.exception.PronunciationDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.UnexpectedUpdateWordExampleCountException;
import com.dnd.spaced.core.admin.application.exception.WordExampleDeletionNotAllowedException;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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
    void 용어를_추가한다() {
        // given
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(
                new PronunciationInfoDto("어써라이제이션", "한글 발음")
        );
        List<String> examples = List.of("example");
        SaveWordDto saveWordDto = new SaveWordDto(
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                pronunciationInfoDtos,
                examples
        );

        // when
        Long actual = adminWordService.saveWord(saveWordDto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 용어_예문을_변경한다() {
        // given
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(
                new PronunciationInfoDto("어써라이제이션", "한글 발음")
        );
        List<String> examples = List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.");
        SaveWordDto saveWordDto = new SaveWordDto(
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                pronunciationInfoDtos,
                examples
        );

        Long wordId = adminWordService.saveWord(saveWordDto);
        Word word = wordRepository.findBy(wordId)
                                  .get();

        // when & then
        assertDoesNotThrow(() -> adminWordService.updateWordExample(
                word.getWordExamples().get(0).getId(),
                "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다.")
        );
    }

    @Test
    void 용어_예문_변경_시_유효한_용어_예문_식별자가_아닌_경우_용어_예문을_변경할_수_없다() {
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
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(new PronunciationInfoDto("어써라이제이션", "한글 발음"));
        List<String> examples = List.of(
                "게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.",
                "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다."
        );
        SaveWordDto saveWordDto = new SaveWordDto(
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                pronunciationInfoDtos,
                examples
        );

        Long wordId = adminWordService.saveWord(saveWordDto);
        Word word = wordRepository.findBy(wordId)
                                  .get();

        // when & then
        assertDoesNotThrow(
                () -> adminWordService.deleteWordExample(word.getId(), word.getWordExamples().get(0).getId())
        );
    }

    @Test
    void 용어_예문_삭제_시_용어_예문의_개수가_최소치라면_용어_예문을_삭제할_수_없다() {
        // given
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(new PronunciationInfoDto("어써라이제이션", "한글 발음"));
        List<String> examples = List.of(
                "게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다."
        );
        SaveWordDto saveWordDto = new SaveWordDto(
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                pronunciationInfoDtos,
                examples
        );

        Long wordId = adminWordService.saveWord(saveWordDto);
        Word word = wordRepository.findBy(wordId)
                                  .get();

        // when & then
        assertThatThrownBy(
                () -> adminWordService.deleteWordExample(word.getId(), word.getWordExamples().get(0).getId())
        ).isInstanceOf(WordExampleDeletionNotAllowedException.class)
         .hasMessage("해당 용어의 예문 개수가 최소치입니다.");
    }

    @Test
    void 용어_발음_정보를_삭제한다() {
        // given
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(
                new PronunciationInfoDto("어써라이제이션", "한글 발음"),
                new PronunciationInfoDto("오써러제이션", "한글 발음")
        );
        List<String> examples = List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.");
        SaveWordDto saveWordDto = new SaveWordDto(
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                pronunciationInfoDtos,
                examples
        );

        Long wordId = adminWordService.saveWord(saveWordDto);
        Word word = wordRepository.findBy(wordId)
                                  .get();

        // when & then
        assertDoesNotThrow(
                () -> adminWordService.deletePronunciation(wordId, word.getPronunciations().get(0).getId())
        );
    }

    @Test
    void 용어_발음_정보_삭제_시_용어_발음_정보의_개수가_최소치라면_용어_발음_정보를_삭제할_수_없다() {
        // given
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(
                new PronunciationInfoDto("어써라이제이션", "한글 발음")
        );
        List<String> examples = List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.");
        SaveWordDto saveWordDto = new SaveWordDto(
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                pronunciationInfoDtos,
                examples
        );

        Long wordId = adminWordService.saveWord(saveWordDto);
        Word word = wordRepository.findBy(wordId)
                                  .get();

        // when & then
        assertThatThrownBy(
                () -> adminWordService.deletePronunciation(wordId, word.getPronunciations().get(0).getId())
        ).isInstanceOf(PronunciationDeletionNotAllowedException.class)
         .hasMessage("해당 용어의 발음 정보 개수가 최소치입니다.");
    }
}
