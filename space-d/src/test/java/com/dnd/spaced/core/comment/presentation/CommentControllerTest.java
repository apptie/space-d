package com.dnd.spaced.core.comment.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
import com.dnd.spaced.core.comment.application.CommentServiceFacade;
import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentContentResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentWriterResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class CommentControllerTest extends CommonControllerSliceTest {

    @Autowired
    CommentServiceFacade commentServiceFacade;

    @Test
    @WithMockUser("1")
    void 댓글_작성_요청_성공_테스트() throws Exception {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어 언제 쓰는건가요?");

        // when & then
        ResultActions resultActions = mockMvc.perform(
                post("/words/{wordId}/comments", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/words/1")
        );

        verify(commentServiceFacade).createComment(anyLong(), anyLong(), any(CreateCommentRequest.class));

        댓글_작성_요청_문서화(resultActions);
    }

    private void 댓글_작성_요청_문서화(ResultActions resultActions) throws Exception {
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
    @WithMockUser("1")
    void 댓글_삭제_요청_성공_테스트() throws Exception {
        // when & then
        ResultActions resultActions = mockMvc.perform(
                delete("/comments/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );

        verify(commentServiceFacade).deleteComment(anyLong(), anyLong());

        댓글_삭제_요청_문서화(resultActions);
    }

    private void 댓글_삭제_요청_문서화(ResultActions resultActions) throws Exception {
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
    @WithMockUser("1")
    void 댓글_수정_요청_성공_테스트() throws Exception {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("이 용어 쓰기는 하는건가요?");

        // when & then
        ResultActions resultActions = mockMvc.perform(
                put("/comments/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                         .contentType(MediaType.APPLICATION_JSON)
                                         .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );

        verify(commentServiceFacade).updateComment(anyLong(), anyLong(), any(UpdateCommentRequest.class));

        댓글_수정_요청_문서화(resultActions);
    }

    private void 댓글_수정_요청_문서화(ResultActions resultActions) throws Exception {
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
    void 댓글_목록_조회_성공_테스트() throws Exception {
        // given
        CommentContentResponse commentContentResponse = new CommentContentResponse(1L, 1L, "이 용어 언제 쓰는건가요?", 0);
        CommentWriterResponse commentWriterResponse = new CommentWriterResponse(1L, "재빠른지구001", "earth.png");
        CommentResponse commentResponse = new CommentResponse(commentContentResponse, commentWriterResponse, false);
        CommentCollectionResponse response = new CommentCollectionResponse(List.of(commentResponse), 1L);

        given(commentServiceFacade.readComments(anyLong(), anyLong(), eq(null), any())).willReturn(response);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/words/{wordId}/comments", 1L).accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("comments").exists(),
                jsonPath("comments[0].commentContent").exists(),
                jsonPath("comments[0].commentContent.commentId", is(1L), Long.class),
                jsonPath("comments[0].commentContent.wordId", is(1L), Long.class),
                jsonPath("comments[0].commentContent.content").value("이 용어 언제 쓰는건가요?"),
                jsonPath("comments[0].commentContent.likeCount", is(0L), Long.class),
                jsonPath("comments[0].writer").exists(),
                jsonPath("comments[0].writer.writerId", is(1L), Long.class),
                jsonPath("comments[0].writer.writerNickname").value("재빠른지구001"),
                jsonPath("comments[0].writer.writerProfileImage").value("earth.png"),
                jsonPath("comments[0].liked").value(false),
                jsonPath("lastCommentId", is(1L), Long.class)
        );

        verify(commentServiceFacade).readComments(any(), anyLong(), any(), any(Pageable.class));

        댓글_전체_조회_문서화(resultActions);
    }

    private void 댓글_전체_조회_문서화(ResultActions resultActions) throws Exception {
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
                                fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록 조회 결과"),
                                fieldWithPath("comments[*].commentContent").type(JsonFieldType.OBJECT).description("댓글 정보"),
                                fieldWithPath("comments[*].commentContent.commentId").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("comments[*].commentContent.wordId").type(JsonFieldType.NUMBER).description("댓글이 추가된 용어 ID"),
                                fieldWithPath("comments[*].commentContent.content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("comments[*].commentContent.likeCount").type(JsonFieldType.NUMBER).description("댓글 좋아요 수"),
                                fieldWithPath("comments[*].writer").type(JsonFieldType.OBJECT).description("작성자 정보"),
                                fieldWithPath("comments[*].writer.writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                fieldWithPath("comments[*].writer.writerNickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("comments[*].writer.writerProfileImage").type(JsonFieldType.STRING).description("작성자 프로필 이미지"),
                                fieldWithPath("comments[*].liked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("lastCommentId").type(JsonFieldType.NUMBER).description("마지막으로 조회한 댓글 ID")
                        )
                )
        );
    }
}
