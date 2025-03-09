package com.dnd.spaced.core.admin.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AdminTodayQuizControllerTest extends CommonControllerSliceTest {

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

        verify(adminTodayQuizService).create();

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
}
