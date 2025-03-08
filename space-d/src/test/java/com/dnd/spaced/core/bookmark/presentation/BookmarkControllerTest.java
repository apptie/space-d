package com.dnd.spaced.core.bookmark.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
import org.springframework.security.test.context.support.WithMockUser;

@SuppressWarnings("NonAsciiCharacters")
class BookmarkControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("1")
    void 북마크_생성_요청_성공_테스트() throws Exception {
        // when & then
        CreateBookmarkRequest request = new CreateBookmarkRequest(1L);

        mockMvc.perform(
                post("/bookmarks").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isNoContent());
    }

    @Test
    @WithMockUser("1")
    void 북마크_삭제_요청_성공_테스트() throws Exception {
        // when & then
        mockMvc.perform(
                delete("/bookmarks/{bookmarkId}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(status().isNoContent());
    }

    @Test
    @WithMockUser("1")
    void 북마크_목록_조회_요청_성공_테스트() throws Exception {
        // given
        BookmarkResponse bookmarkResponse = new BookmarkResponse(1L, 1L, 1L, LocalDateTime.now());
        BookmarkCollectionResponse response = new BookmarkCollectionResponse(List.of(bookmarkResponse), 1L);

        given(bookmarkService.findAllBy(anyLong(), any(ReadAllBookmarkRequest.class), any(Pageable.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(
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
    }
}
