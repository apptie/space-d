package com.dnd.spaced.config.docs.snippet.exceptions.quiz;

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

class QuizExceptionControllerTest extends CommonExceptionControllerTest {

    @Test
    void exceptions() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/test/quizzes/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultActions.andReturn();
        QuizExceptionDocs data = findExceptionData(mvcResult, QuizExceptionDocs.class);

        resultActions.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.createQuizException").withSubsectionId("createQuizException"),
                                        attributes(key("title").value("`POST /quizzes` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.createQuizException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.gradeQuizException").withSubsectionId("gradeQuizException"),
                                        attributes(key("title").value("`POST /quizzes/{quizId}/graded-answers` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.gradeQuizException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readGradedAnswerAllByQuizIdException").withSubsectionId("readGradedAnswerAllByQuizIdException"),
                                        attributes(key("title").value("`GET /quizzes/graded-answers` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readGradedAnswerAllByQuizIdException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readGradedAnswerAllException").withSubsectionId("readGradedAnswerAllException"),
                                        attributes(key("title").value("`GET /quizzes/{quizId}/graded-answers` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readGradedAnswerAllException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.findQuizByException").withSubsectionId("findQuizByException"),
                                        attributes(key("title").value("`GET /quizzes/{quizId}` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.findQuizByException())
                                ),
                                customResponseFields(
                                        "exception-response",
                                        beneathPath("data.readQuizzesException").withSubsectionId("readQuizzesException"),
                                        attributes(key("title").value("`GET /quizzes` 예외 상황")),
                                        exceptionConvertFieldDescriptor(data.readQuizzesException())
                                )
                        )
                );
    }
}
