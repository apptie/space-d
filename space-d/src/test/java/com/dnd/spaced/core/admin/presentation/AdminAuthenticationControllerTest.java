package com.dnd.spaced.core.admin.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.admin.application.dto.request.UpdateBlacklistTokenRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AdminAuthenticationControllerTest extends CommonControllerSliceTest {

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
        ).andExpectAll(status().isCreated());

        verify(blacklistTokenService, times(1)).register(anyLong());

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
}
