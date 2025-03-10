package com.dnd.spaced.core.bookmark.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse.BookmarkResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class BookmarkControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("1")
    void 북마크_생성_요청_성공_테스트() throws Exception {
        // given
        CreateBookmarkRequest request = new CreateBookmarkRequest(1L);

        // when & then

        ResultActions resultActions = mockMvc.perform(
                post("/bookmarks").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isNoContent());

        verify(bookmarkService).createBookmark(anyLong(), any(CreateBookmarkRequest.class));

        북마크_생성_요청_문서화(resultActions);
    }

    private void 북마크_생성_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("wordId").description("북마크를 추가할 용어 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 북마크_삭제_요청_성공_테스트() throws Exception {
        // when & then
        ResultActions resultActions = mockMvc.perform(
                delete("/bookmarks/{bookmarkId}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(status().isNoContent());

        verify(bookmarkService).deleteBookmark(anyLong(), anyLong());

        북마크_삭제_요청_문서화(resultActions);
    }

    private void 북마크_삭제_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("bookmarkId").description("삭제할 북마크 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 북마크_목록_조회_요청_성공_테스트() throws Exception {
        // given
        BookmarkResponse bookmarkResponse = new BookmarkResponse(1L, 1L, 1L, LocalDateTime.now());
        BookmarkCollectionResponse response = new BookmarkCollectionResponse(List.of(bookmarkResponse), 1L);

        given(bookmarkService.readBookmarks(anyLong(), any(ReadAllBookmarkRequest.class), any(Pageable.class)))
                .willReturn(response);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/bookmarks").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isOk(),
                jsonPath("bookmarks").exists(),
                jsonPath("bookmarks[0].bookmarkId", is(1L), Long.class),
                jsonPath("bookmarks[0].accountId", is(1L), Long.class),
                jsonPath("bookmarks[0].wordId", is(1L), Long.class),
                jsonPath("bookmarks[0].createdAt").exists(),
                jsonPath("lastBookmarkId", is(1L), Long.class)
        );

        verify(bookmarkService).readBookmarks(anyLong(), any(ReadAllBookmarkRequest.class), any(Pageable.class));

        북마크_목록_조회_요청_문서화(resultActions);
    }

    private void 북마크_목록_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        responseFields(
                                fieldWithPath("bookmarks").type(JsonFieldType.ARRAY).description("북마크 목록"),
                                fieldWithPath("bookmarks[*].bookmarkId").type(JsonFieldType.NUMBER).description("북마크 ID"),
                                fieldWithPath("bookmarks[*].accountId").type(JsonFieldType.NUMBER).description("북마크 생성자 ID"),
                                fieldWithPath("bookmarks[*].wordId").type(JsonFieldType.NUMBER).description("용어 ID"),
                                fieldWithPath("bookmarks[*].createdAt").type(JsonFieldType.STRING).description("용어 생성 일자"),
                                fieldWithPath("lastBookmarkId").type(JsonFieldType.NUMBER).description("마지막으로 조회한 북마크 ID")
                        )
                )
        );
    }
}
