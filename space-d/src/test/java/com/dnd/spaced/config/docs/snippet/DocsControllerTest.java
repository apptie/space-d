package com.dnd.spaced.config.docs.snippet;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.CustomResponseFieldsSnippet;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.enums.EnumDocs;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionDocs;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

class DocsControllerTest extends CommonControllerSliceTest {

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
                                      attributes(key("title").value("Company")),
                                      enumConvertFieldDescriptor(data.getCompany())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.jobGroup").withSubsectionId("jobGroup"),
                                      attributes(key("title").value("JobGroup")),
                                      enumConvertFieldDescriptor(data.getJobGroup())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.experience").withSubsectionId("experience"),
                                      attributes(key("title").value("Experience")),
                                      enumConvertFieldDescriptor(data.getExperience())
                              )
                      ));
    }

    @Test
    void exceptions() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/test/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = result.andReturn();
        ExceptionDocs data = findExceptionData(mvcResult);

        result.andExpect(status().isOk())
              .andDo(
                      restDocs.document(
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.authProfileException").withSubsectionId("authProfileException"),
                                      attributes(key("title").value("`/auths/profile` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getAuthProfileException())
                              )
                      )
              );
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
                new TypeReference<>() {
                }
        );

        return apiResponseDto.data();
    }

    private static FieldDescriptor[] exceptionConvertFieldDescriptor(Map<String, ExceptionContent> exceptionValues) {
        return exceptionValues.entrySet()
                .stream()
                .map(
                        exceptionValue -> fieldWithPath(exceptionValue.getKey()).description(exceptionValue.getValue().httpStatus().name())
                                                                                .attributes(
                                                                                        field("status", String.valueOf(exceptionValue.getValue().httpStatus().value())),
                                                                                        field("message", exceptionValue.getValue().message())
                                                                                )
                ).toArray(FieldDescriptor[]::new);
    }

    private ExceptionDocs findExceptionData(MvcResult result) throws IOException {
        CommonDocsResponse<ExceptionDocs> apiResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        return apiResponseDto.data();
    }
}
