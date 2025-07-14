package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.PersistWordDto;
import com.dnd.spaced.core.word.domain.enums.Category;
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
class CreateWordServiceTest {

    @Autowired
    CreateWordService createWordService;

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
        PersistWordDto actual = createWordService.createWord(request);

        // then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.category()).isEqualTo(Category.DEVELOP)
        );
    }
}
