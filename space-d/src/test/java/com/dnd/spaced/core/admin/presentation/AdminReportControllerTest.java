package com.dnd.spaced.core.admin.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.admin.application.dto.request.ProcessReportRequest;
import com.dnd.spaced.core.admin.application.dto.request.ReadAllReportSearchRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse.ReportResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AdminReportControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser(value = "1", roles = "ADMIN")
    void 신고_목록_조회_요청_성공_테스트() throws Exception {
        // given
        ReportResponse reportResponse = new ReportResponse(6L, 1L, 3L, "기타");
        ReportCollectionResponse reportCollectionResponse = new ReportCollectionResponse(List.of(reportResponse), 1L);
        given(adminReportService.findAllBy(any(ReadAllReportSearchRequest.class))).willReturn(reportCollectionResponse);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/admin/reports").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                        .param("reportStatus", "기타")
                        .param("lastReportId", "5")
        ).andExpectAll(
                status().isOk(),
                jsonPath("reports").exists(),
                jsonPath("reports[0].id", is(6L), Long.class),
                jsonPath("reports[0].commentId", is(1L), Long.class),
                jsonPath("reports[0].reporterId", is(3L), Long.class),
                jsonPath("reports[0].reportStatus").value("기타"),
                jsonPath("lastReportId", is(1L), Long.class)
        );

        verify(adminReportService, times(1))
                .findAllBy(any(ReadAllReportSearchRequest.class));

        신고_목록_조회_요청_문서화(resultActions);
    }

    private void 신고_목록_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        queryParameters(
                                parameterWithName("reportStatus").optional().description("조회할 신고 상태")
                                                             .attributes(field("constraints", generateLinkCode(DocsUrl.REPORT_STATUS))),
                                parameterWithName("lastReportId").optional().description("마지막으로 조회한 신고 ID")
                        ),
                        responseFields(
                                fieldWithPath("reports").type(JsonFieldType.ARRAY).description("신고 목록"),
                                fieldWithPath("reports[*].id").type(JsonFieldType.NUMBER).description("신고 ID"),
                                fieldWithPath("reports[*].commentId").type(JsonFieldType.NUMBER).description("신고 대상 댓글 ID"),
                                fieldWithPath("reports[*].reporterId").type(JsonFieldType.NUMBER).description("신고자 ID"),
                                fieldWithPath("reports[*].reportStatus").type(JsonFieldType.STRING).description("신고 사유"),
                                fieldWithPath("lastReportId").type(JsonFieldType.NUMBER).optional().description("마지막으로 조회한 신고 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser(value = "1", roles = "ADMIN")
    void 신고_처리_요청_성공_테스트() throws Exception {
        // when & then
        ProcessReportRequest request = new ProcessReportRequest("신고 처리");

        ResultActions resultActions = mockMvc.perform(
                post("/admin/reports/{reportId}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isNoContent());

        verify(adminReportService, times(1))
                .process(anyLong(), any(ProcessReportRequest.class));

        신고_처리_요청_문서화(resultActions);
    }

    private void 신고_처리_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("reportId").description("처리할 신고 ID")
                        ),
                        requestFields(
                                fieldWithPath("reportStatus").optional().description("처리할 신고 상태")
                                                             .attributes(field("constraints", generateLinkCode(DocsUrl.REPORT_STATUS)))
                        )
                )
        );
    }
}
