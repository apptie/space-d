package com.dnd.spaced.config.docs.snippet.exceptions.account;

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

class AccountExceptionControllerTest extends CommonExceptionControllerTest {

    @Test
    void exceptions() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/test/accounts/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultActions.andReturn();
        AccountExceptionDocs data = findExceptionData(mvcResult, AccountExceptionDocs.class);

        resultActions.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.withdrawalException").withSubsectionId("withdrawalException"),
                                        attributes(key("title").value("`DELETE /accounts/withdrawal` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.withdrawalException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.changeCareerInfoException").withSubsectionId("changeCareerInfoException"),
                                        attributes(key("title").value("`PUT /accounts/career-info` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.changeCareerInfoException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.changeProfileInfoException").withSubsectionId("changeProfileInfoException"),
                                        attributes(key("title").value("`PUT /accounts/profile-info` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.changeProfileInfoException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readAccountException").withSubsectionId("readAccountException"),
                                        attributes(key("title").value("`GET /accounts` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readAccountException())
                                )
                        )
                );
    }
}
