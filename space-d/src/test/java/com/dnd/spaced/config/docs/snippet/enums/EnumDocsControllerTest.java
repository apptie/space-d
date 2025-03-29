package com.dnd.spaced.config.docs.snippet.enums;

import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.CustomResponseFieldsSnippet;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

class EnumDocsControllerTest extends CommonControllerSliceTest {

    @Test
    void enums() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/test/enums").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = result.andReturn();
        EnumDocs data = findEnumData(mvcResult);

        result.andExpect(status().isOk())
              .andDo(
                      restDocs.document(
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.company").withSubsectionId("company"),
                                      attributes(key("title").value("Company 허용 값")),
                                      enumConvertFieldDescriptor(data.company())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.jobGroup").withSubsectionId("jobGroup"),
                                      attributes(key("title").value("JobGroup 허용 값")),
                                      enumConvertFieldDescriptor(data.jobGroup())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.experience").withSubsectionId("experience"),
                                      attributes(key("title").value("Experience 허용 값")),
                                      enumConvertFieldDescriptor(data.experience())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.category").withSubsectionId("category"),
                                      attributes(key("title").value("Category 허용 값")),
                                      enumConvertFieldDescriptor(data.category())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.profileImageName").withSubsectionId("profileImageName"),
                                      attributes(key("title").value("ProfileImageName 허용 값")),
                                      enumConvertFieldDescriptor(data.profileImageName())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.pronunciationType").withSubsectionId("pronunciationType"),
                                      attributes(key("title").value("PronunciationType 허용 값")),
                                      enumConvertFieldDescriptor(data.pronunciationType())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.quizCategory").withSubsectionId("quizCategory"),
                                      attributes(key("title").value("QuizCategory 허용 값")),
                                      enumConvertFieldDescriptor(data.quizCategory())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.reportReason").withSubsectionId("reportReason"),
                                      attributes(key("title").value("ReportReason 허용 값")),
                                      enumConvertFieldDescriptor(data.reportReason())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.reportStatus").withSubsectionId("reportStatus"),
                                      attributes(key("title").value("ReportStatus 허용 값")),
                                      enumConvertFieldDescriptor(data.reportStatus())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.todayQuizStatus").withSubsectionId("todayQuizStatus"),
                                      attributes(key("title").value("TodayQuizStatus 허용 값")),
                                      enumConvertFieldDescriptor(data.todayQuizStatus())
                              )
                      ));
    }

    public static CustomResponseFieldsSnippet customResponseFields(
            String type,
            PayloadSubsectionExtractor<?> subsectionExtractor,
            Map<String, Object> attributes,
            FieldDescriptor... descriptors
    ) {
        return new CustomResponseFieldsSnippet(
                type,
                subsectionExtractor,
                Arrays.asList(descriptors),
                attributes,
                true
        );
    }

    private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
        return enumValues.entrySet()
                         .stream()
                         .map(enumValue -> fieldWithPath(enumValue.getKey()).description(enumValue.getValue()))
                         .toArray(FieldDescriptor[]::new);
    }

    private EnumDocs findEnumData(MvcResult result) throws IOException {
        CommonDocsResponse<EnumDocs> apiResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                objectMapper.getTypeFactory().constructParametricType(CommonDocsResponse.class, EnumDocs.class)
        );

        return apiResponseDto.data();
    }
}
