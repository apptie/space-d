package com.dnd.spaced.core.skill.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.skill.application.dto.response.SkillResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;

@SuppressWarnings("NonAsciiCharacters")
class SkillControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("1")
    void 스킬_조회_요청_성공_테스트() throws Exception {
        // given
        SkillResponse response = new SkillResponse(
                1L,
                5L,
                1L,
                0L,
                1L,
                20.0d,
                0.0d
        );
        given(skillService.findBy(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/skills").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("accountId", is(1L), Long.class),
                       jsonPath("submitQuizQuestionCount", is(5L), Long.class),
                       jsonPath("quizQuestionCorrectCount", is(1L), Long.class),
                       jsonPath("submitTodayQuizQuestionCount", is(0L), Long.class),
                       jsonPath("todayQuizQuestionCorrectCount", is(1L), Long.class),
                       jsonPath("totalTodayQuizQuestionCorrectPercent", is(0.0d), double.class)
               );
    }
}
