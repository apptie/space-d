package com.dnd.spaced.core.report.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.report.application.dto.request.ReportRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class ReportControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("1")
    void 신고_요청_성공_테스트() throws Exception {
        // when & then
        ReportRequest request = new ReportRequest(1L, "기타");

        ResultActions resultActions = mockMvc.perform(
                post("/reports").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());

        신고_요청_문서화(resultActions);
    }

    private void 신고_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("신고 댓글 ID"),
                                fieldWithPath("cause").type(JsonFieldType.STRING).description("신고 사유")
                                                      .attributes(field("constraints", generateLinkCode(DocsUrl.REPORT_REASON)))
                        )
                )
        );
    }
}
