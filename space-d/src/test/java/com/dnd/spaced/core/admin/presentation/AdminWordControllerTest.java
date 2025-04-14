package com.dnd.spaced.core.admin.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.admin.application.AdminWordService;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.dto.request.UpdateWordExampleRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AdminWordControllerTest extends CommonControllerSliceTest {

    @Autowired
    AdminWordService adminWordService;

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

        verify(adminWordService).createWord(any(CreateWordRequest.class));

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

        verify(adminWordService).updateWordExample(anyLong(), anyString());

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
                                fieldWithPath("content").description("변경할 용어 예문")
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

        verify(adminWordService).deleteWordExample(anyLong(), anyLong());

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

        verify(adminWordService).deletePronunciation(anyLong(), anyLong());

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
}
