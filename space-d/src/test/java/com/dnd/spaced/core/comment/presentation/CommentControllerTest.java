package com.dnd.spaced.core.comment.presentation;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto.CommentInfoDto;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto.WriterInfoDto;
import com.dnd.spaced.core.comment.presentation.dto.request.SaveCommentRequest;
import com.dnd.spaced.core.comment.presentation.dto.request.UpdateCommentRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SuppressWarnings("NonAsciiCharacters")
class CommentControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("account")
    void save_성공_테스트() throws Exception {
        // given
        SaveCommentRequest request = new SaveCommentRequest("content");

        // when & then
        mockMvc.perform(
                post("/words/{wordId}/comments", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/words/1")
        );
    }

    @Test
    @WithMockUser("account")
    void delete_성공_테스트() throws Exception {
        // when & then
        mockMvc.perform(
                delete("/comments/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    @WithMockUser("account")
    void update_성공_테스트() throws Exception {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("change content");

        // when & then
        mockMvc.perform(
                put("/comments/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    void readAllBy_성공_테스트() throws Exception {
        // given
        CommentInfoDto commentInfoDto = new CommentInfoDto(1L, 1L, "content", 0);
        WriterInfoDto writerInfoDto = new WriterInfoDto("accountId", "writer", "profileImage");
        ReadAllCommentDto readAllCommentDto = new ReadAllCommentDto(commentInfoDto, writerInfoDto, false);

        given(commentService.readAllBy(eq(null), anyLong(), eq(null), any())).willReturn(List.of(readAllCommentDto));

        // when & then
        mockMvc.perform(
                get("/words/{wordId}/comments", 1L).accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("comments").exists(),
                jsonPath("comments[*].commentInfo").exists(),
                jsonPath("comments[*].commentInfo.id").exists(),
                jsonPath("comments[*].commentInfo.content").value("content"),
                jsonPath("comments[*].commentInfo.likeCount").value(0),
                jsonPath("comments[*].writerInfo").exists(),
                jsonPath("comments[*].writerInfo.id").value("accountId"),
                jsonPath("comments[*].writerInfo.writerNickname").value("writer"),
                jsonPath("comments[*].writerInfo.writerProfileImage").value("profileImage")
        );
    }
}
