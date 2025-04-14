package com.dnd.spaced.config.docs.snippet.exceptions.admin;

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

class AdminExceptionControllerTest extends CommonExceptionControllerTest {

    @Test
    void exceptions() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/test/admin/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultActions.andReturn();
        AdminExceptionDocs data = findExceptionData(mvcResult, AdminExceptionDocs.class);

        resultActions.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.createWordException").withSubsectionId("createWordException"),
                                        attributes(key("title").value("`POST /admin/words` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.createWordException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.updateWordExampleException").withSubsectionId("updateWordExampleException"),
                                        attributes(key("title").value("`PATCH /admin/words/examples/{id}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.updateWordExampleException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.deleteWordExampleException").withSubsectionId("deleteWordExampleException"),
                                        attributes(key("title").value("`DELETE /admin/words/examples/{id}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.deleteWordExampleException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.deletePronunciationException").withSubsectionId("deletePronunciationException"),
                                        attributes(key("title").value("`DELETE /admin/words/pronunciations/{id}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.deletePronunciationException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.processReportException").withSubsectionId("processReportException"),
                                        attributes(key("title").value("`POST /reports/{id}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.processReportException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.createTodayQuizException").withSubsectionId("createTodayQuizException"),
                                        attributes(key("title").value("`POST /admin/today-quizzes` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.createTodayQuizException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.deleteWordException").withSubsectionId("deleteWordException"),
                                        attributes(key("title").value("`DELETED /admin/words/{wordId}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.deleteWordException())
                                )
                        )
                );
    }
}
