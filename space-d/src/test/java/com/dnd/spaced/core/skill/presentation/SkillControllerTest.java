package com.dnd.spaced.core.skill.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.skill.application.dto.response.SkillResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

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
                0L,
                20.0d,
                0.0d
        );
        given(skillService.readSkill(anyLong())).willReturn(response);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                                                     get("/skills").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                             )
                                             .andExpectAll(
                                                     status().isOk(),
                                                     jsonPath("accountId", is(1L), Long.class),
                                                     jsonPath("submitQuizQuestionCount", is(5L), Long.class),
                                                     jsonPath("quizQuestionCorrectCount", is(1L), Long.class),
                                                     jsonPath("submitTodayQuizQuestionCount", is(0L), Long.class),
                                                     jsonPath("todayQuizQuestionCorrectCount", is(0L), Long.class),
                                                     jsonPath("totalTodayQuizQuestionCorrectPercent", is(0.0d), double.class)
                                             );

        verify(skillService).readSkill(anyLong());

        스킬_조회_요청_문서화(resultActions);
    }

    private void 스킬_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        responseFields(
                                fieldWithPath("accountId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("submitQuizQuestionCount").type(JsonFieldType.NUMBER)
                                                                        .description("회원이 제출한 퀴즈 문제 개수"),
                                fieldWithPath("quizQuestionCorrectCount").type(JsonFieldType.NUMBER)
                                                                         .description("회원이 맞춘 퀴즈 문제 개수"),
                                fieldWithPath("submitTodayQuizQuestionCount").type(JsonFieldType.NUMBER)
                                                                             .description("회원이 제출한 오늘의 퀴즈 문제 개수"),
                                fieldWithPath("todayQuizQuestionCorrectCount").type(JsonFieldType.NUMBER)
                                                                              .description("회원이 제출한 오늘의 퀴즈 문제 개수"),
                                fieldWithPath("totalQuizQuestionCorrectPercent").type(JsonFieldType.NUMBER).description(
                                        "전체 퀴즈 문제 중 회원이 맞춘 퀴즈 문제 비율(퍼센트)"),
                                fieldWithPath("totalTodayQuizQuestionCorrectPercent").type(JsonFieldType.NUMBER)
                                                                                     .description(
                                                                                             "전체 오늘의 퀴즈 문제 중 회원이 맞춘 오늘의 퀴즈 문제 비율(퍼센트)")
                        )
                )
        );
    }
}
