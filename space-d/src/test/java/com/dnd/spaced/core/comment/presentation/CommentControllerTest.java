package com.dnd.spaced.core.comment.presentation;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto.CommentInfoDto;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto.WriterInfoDto;
import com.dnd.spaced.core.comment.presentation.dto.request.SaveCommentRequest;
import com.dnd.spaced.core.comment.presentation.dto.request.UpdateCommentRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class CommentControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("account")
    void save_성공_테스트() throws Exception {
        // given
        SaveCommentRequest request = new SaveCommentRequest("content");

        // when & then
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/words/{wordId}/comments", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/words/1")
        );

        save_문서화(resultActions);
    }

    private void save_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("wordId").description("댓글을 추가할 용어 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글 내용")
                        )
                )
        );
    }

    @Test
    @WithMockUser("account")
    void delete_성공_테스트() throws Exception {
        // when & then
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/comments/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );

        delete_문서화(resultActions);
    }

    private void delete_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("삭제할 댓글 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser("account")
    void update_성공_테스트() throws Exception {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("change content");

        // when & then
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/comments/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                         .contentType(MediaType.APPLICATION_JSON)
                                         .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );

        update_문서화(resultActions);
    }

    private void update_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("수정할 댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("수정할 댓글 내용")
                        )
                )
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
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/words/{wordId}/comments", 1L).accept(MediaType.APPLICATION_JSON)
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

        readAllBy_문서화(resultActions);
    }

    private void readAllBy_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token").optional()
                        ),
                        pathParameters(
                                parameterWithName("wordId").description("댓글 목록을 조회할 용어 ID")
                        ),
                        queryParameters(
                                parameterWithName("lastCommentId").description("마지막으로 조회한 댓글 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("comments").description("댓글 목록 조회 결과"),
                                fieldWithPath("comments[*].commentInfo").description("댓글 정보"),
                                fieldWithPath("comments[*].commentInfo.id").description("댓글 ID"),
                                fieldWithPath("comments[*].commentInfo.wordId").description("댓글이 추가된 용어 ID"),
                                fieldWithPath("comments[*].commentInfo.content").description("댓글 내용"),
                                fieldWithPath("comments[*].commentInfo.likeCount").description("댓글 좋아요 수"),
                                fieldWithPath("comments[*].writerInfo").description("작성자 정보"),
                                fieldWithPath("comments[*].writerInfo.id").description("작성자 ID"),
                                fieldWithPath("comments[*].writerInfo.writerNickname").description("작성자 닉네임"),
                                fieldWithPath("comments[*].writerInfo.writerProfileImage").description("작성자 프로필 이미지")
                        )
                )
        );
    }
}
