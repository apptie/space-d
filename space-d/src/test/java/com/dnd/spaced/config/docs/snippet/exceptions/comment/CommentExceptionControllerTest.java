package com.dnd.spaced.config.docs.snippet.exceptions.comment;

import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.docs.snippet.CommonExceptionControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

class CommentExceptionControllerTest extends CommonExceptionControllerTest {

    @Test
    void exceptions() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/test/comments/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultActions.andReturn();
        CommentExceptionDocs data = findExceptionData(mvcResult, CommentExceptionDocs.class);

        resultActions.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.saveCommentException").withSubsectionId("saveCommentException"),
                                        attributes(key("title").value("`POST /words/{wordId}/comments` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.saveCommentException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.deleteCommentException").withSubsectionId("deleteCommentException"),
                                        attributes(key("title").value("`DELETE /comments/{id}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.deleteCommentException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.updateCommentException").withSubsectionId("updateCommentException"),
                                        attributes(key("title").value("`PUT /comments/{id}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.updateCommentException())
                                )
                        )
                );
    }
}
