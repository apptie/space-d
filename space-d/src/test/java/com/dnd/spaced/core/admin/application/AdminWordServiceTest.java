package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto.PronunciationInfoDto;
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

    @Test
    void saveWord_메서드는_유효한_CreateWordDto를_전달하면_Word를_초기화하고_영속화한다() {
        // given
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(new PronunciationInfoDto("어써라이제이션", "한글 발음"));
        List<String> examples = List.of("example");
        SaveWordDto saveWordDto = new SaveWordDto(
                "name",
                "word meaning",
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
    void updateWordExample_메서드는_지정한_id에_해당하는_WordExample의_example을_전달한_example로_변환한다() {
        // when & then
        assertDoesNotThrow(() -> adminWordService.updateWordExample(1L, "changed word example"));
    }

    @Test
    void deleteWordExample_메서드는_지정한_id에_해당하는_WordExample을_삭제한다() {
        // when & then
        assertDoesNotThrow(() -> adminWordService.deleteWordExample(1L));
    }

    @Test
    void deletePronunciation_메서드는_지정한_id에_해당하는_Pronunciation을_삭제한다() {
        // when & then
        assertDoesNotThrow(() -> adminWordService.deletePronunciation(1L));
    }
}
