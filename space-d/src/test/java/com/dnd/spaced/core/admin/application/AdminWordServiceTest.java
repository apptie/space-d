package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto.PronunciationInfoDto;
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
    void 용어_예문을_삭제한다() {
        // given
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(new PronunciationInfoDto("어써라이제이션", "한글 발음"));
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
        assertDoesNotThrow(() -> adminWordService.deleteWordExample(word.getWordExamples().get(0).getId()));
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
        assertDoesNotThrow(() -> adminWordService.deletePronunciation(word.getPronunciations().get(0).getId()));
    }
}
