package com.dnd.spaced.config.docs.snippet.exceptions.auth;

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

class TokenExceptionControllerTest extends CommonExceptionControllerTest {

    @Test
    void exceptions() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/test/tokens/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultActions.andReturn();
        TokenExceptionDocs data = findExceptionData(mvcResult, TokenExceptionDocs.class);

        resultActions.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.commonTokenException").withSubsectionId("commonTokenException"),
                                        attributes(key("title").value("토큰 공통 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.commonTokenException())
                                )
                        )
                );
    }
}
