package com.dnd.spaced.core.admin.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.dto.request.ProcessReportRequest;
import com.dnd.spaced.core.admin.application.dto.request.ReadAllReportSearchRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse.ReportResponse;
import com.dnd.spaced.core.admin.application.dto.request.UpdateBlacklistTokenRequest;
import com.dnd.spaced.core.admin.application.dto.request.UpdateWordExampleRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AdminControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser(value = "1", roles = "ADMIN")
    void 토큰_블랙리스트_등록_요청_성공_테스트() throws Exception {
        // given
        UpdateBlacklistTokenRequest request = new UpdateBlacklistTokenRequest(1L);

        // when & then
        ResultActions resultAction = mockMvc.perform(
                post("/admin/blacklist-token").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        );

        토큰_블랙리스트_등록_요청_문서화(resultAction);
    }

    private void 토큰_블랙리스트_등록_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("accountId").attributes(field("constraints", "가입한 회원 ID만 가능"))
                                                          .description("블랙리스트 토큰으로 등록할 회원 ID")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("생성한 오늘의 퀴즈를 확인할 수 있는 API")
                        )
                )
        );
    }

    @Test
    @WithMockUser(value = "1", roles = "ADMIN")
    void 용어_등록_요청_성공_테스트() throws Exception {
        // given
        List<CreatePronunciationRequest> pronunciationInfo = List.of(
                new CreatePronunciationRequest("어써라이제이션", "한글 발음")
        );
        List<String> example = List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.");
        CreateWordRequest request = new CreateWordRequest(
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                pronunciationInfo,
                example
        );

        given(adminWordService.createWord(any(CreateWordRequest.class))).willReturn(1L);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                post("/admin/words").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().stringValues("Location", "/words/1")
        );

        용어_등록_요청_문서화(resultActions);
    }

    private void 용어_등록_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("name").description("용어 이름"),
                                fieldWithPath("meaning").description("용어 뜻"),
                                fieldWithPath("categoryName").attributes(
                                                                     field("constraints", generateLinkCode(DocsUrl.CATEGORY)))
                                                             .description("용어 카테고리 이름"),
                                fieldWithPath("pronunciations").description("용어 발음 정보"),
                                fieldWithPath("pronunciations[*].pronunciation").description("용어 발음"),
                                fieldWithPath("pronunciations[*].typeName").attributes(
                                                                                   field("constraints", generateLinkCode(DocsUrl.PRONUNCIATION_TYPE)))
                                                                           .description("용어 발음 타입"),
                                fieldWithPath("examples").description("용어 예문")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("용어 요청 Location 헤더")
                        )
                )
        );
    }

    @Test
    @WithMockUser(value = "1", roles = "ADMIN")
    void 용어_예문_변경_요청_성공_테스트() throws Exception {
        // given
        UpdateWordExampleRequest request = new UpdateWordExampleRequest(
                "이 기능은 일반 사용자의 Authorization 범위를 벗어나므로, 관리자 권한이 필요합니다."
        );

        // when & then
        ResultActions resultActions = mockMvc.perform(
                patch("/admin/words/examples/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                       .contentType(MediaType.APPLICATION_JSON)
                                                       .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );

        용어_예문_변경_요청_문서화(resultActions);
    }

    private void 용어_예문_변경_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("변경하고자 하는 용어 예문 ID")
                        ),
                        requestFields(
                                fieldWithPath("example").description("변경할 용어 예문")
                        )
                )
        );
    }

    @Test
    @WithMockUser(value = "1", roles = "ADMIN")
    void 용어_예문_삭제_요청_성공_테스트() throws Exception {
        // when & then
        ResultActions resultActions = mockMvc.perform(
                delete("/admin/words/{wordId}/examples/{exampleId}", 1L, 1L).header(HttpHeaders.AUTHORIZATION,
                        "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );

        용어_예문_삭제_요청_문서화(resultActions);
    }

    private void 용어_예문_삭제_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("wordId").description("삭제하고자 하는 용어 예문을 가진 용어 ID"),
                                parameterWithName("exampleId").description("삭제하고자 하는 용어 예문 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser(value = "1", roles = "ADMIN")
    void 용어_발음_정보_삭제_요청_성공_테스트() throws Exception {
        // when & then
        ResultActions resultAction = mockMvc.perform(
                delete("/admin/words/{wordId}/pronunciations/{pronunciationId}", 1L, 1L).header(
                        HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );

        용어_발음_정보_삭제_요청_문서화(resultAction);
    }

    private void 용어_발음_정보_삭제_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("wordId").description("삭제하고자 하는 용어 발음 정보를 가진 용어 ID"),
                                parameterWithName("pronunciationId").description("삭제하고자 하는 용어 발음 정보 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser(value = "1", roles = "ADMIN")
    void 오늘의_퀴즈_수동_생성_요청_성공_테스트() throws Exception {
        // given
        given(adminTodayQuizService.create()).willReturn(1L);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                post("/admin/today-quizzes").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/today-quizzes/1")
        );

        오늘의_퀴즈_수동_생성_요청_문서화(resultActions);
    }

    private void 오늘의_퀴즈_수동_생성_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("생성한 오늘의 퀴즈를 확인할 수 있는 API")
                        )
                )
        );
    }

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
