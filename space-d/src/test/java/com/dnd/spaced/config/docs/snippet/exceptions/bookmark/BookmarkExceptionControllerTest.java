package com.dnd.spaced.config.docs.snippet.exceptions.bookmark;

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

class BookmarkExceptionControllerTest extends CommonExceptionControllerTest {

    @Test
    void exceptions() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/test/bookmarks/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultActions.andReturn();
        BookmarkExceptionDocs data = findExceptionData(mvcResult, BookmarkExceptionDocs.class);

        resultActions.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.createBookmarkException").withSubsectionId("createBookmarkException"),
                                        attributes(key("title").value("`POST /bookmarks` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.createBookmarkException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.deleteBookmarkException").withSubsectionId("deleteBookmarkException"),
                                        attributes(key("title").value("`DELETE /bookmarks/{bookmarkId}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.deleteBookmarkException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readBookmarksException").withSubsectionId("readBookmarksException"),
                                        attributes(key("title").value("`GET /bookmarks` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readBookmarksException())
                                )
                        )
                );
    }
}
