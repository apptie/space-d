package com.dnd.spaced.config.docs.snippet.exceptions.todayquiz;

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

class TodayQuizExceptionControllerTest extends CommonExceptionControllerTest {

    @Test
    void exceptions() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/test/today-quizzes/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultActions.andReturn();
        TodayQuizExceptionDocs data = findExceptionData(mvcResult, TodayQuizExceptionDocs.class);

        resultActions.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readLatestTodayQuizException").withSubsectionId("readLatestTodayQuizException"),
                                        attributes(key("title").value("`GET /today-quizzes/latest` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readLatestTodayQuizException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readTodayQuizException").withSubsectionId("readTodayQuizException"),
                                        attributes(key("title").value("`GET /today-quizzes/{todayQuizId}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readTodayQuizException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.gradeTodayQuizException").withSubsectionId("gradeTodayQuizException"),
                                        attributes(key("title").value("`POST /today-quizzes/{todayQuizId}/graded-answers` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.gradeTodayQuizException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readTargetTodayQuizGradedAnswersException").withSubsectionId("readTargetTodayQuizGradedAnswersException"),
                                        attributes(key("title").value("`GET /today-quizzes/{todayQuizId}/graded-answers` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readTargetTodayQuizGradedAnswersException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readTodayQuizGradedAnswersException").withSubsectionId("readTodayQuizGradedAnswersException"),
                                        attributes(key("title").value("`GET /today-quizzes/graded-answers` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readTodayQuizGradedAnswersException())
                                )
                        )
                );
    }
}
