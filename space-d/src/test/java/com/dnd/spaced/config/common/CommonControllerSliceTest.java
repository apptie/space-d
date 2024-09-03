package com.dnd.spaced.config.common;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.dnd.spaced.config.docs.snippet.DocsController;
import com.dnd.spaced.config.docs.RestDocsConfiguration;
import com.dnd.spaced.core.auth.application.InitAccountInfoService;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.presentation.AuthController;
import com.dnd.spaced.global.auth.AuthStore;
import com.dnd.spaced.global.auth.interceptor.AuthInterceptor;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfoArgumentResolver;
import com.dnd.spaced.global.exception.GlobalControllerAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = {
                AuthController.class, DocsController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthInterceptor.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthAccountInfoArgumentResolver.class)
        }
)
@Import(RestDocsConfiguration.class)
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CommonControllerSliceTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected RestDocumentationContextProvider provider;

    @MockBean
    protected TokenDecoder tokenDecoder;

    @Autowired
    protected AuthController authController;

    @MockBean
    protected InitAccountInfoService initAccountInfoService;

    @Autowired
    protected DocsController commonDocsController;

    protected MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        AuthStore store = new AuthStore();
        AuthInterceptor authInterceptor = new AuthInterceptor(store);
        AuthAccountInfoArgumentResolver authAccountInfoArgumentResolver = new AuthAccountInfoArgumentResolver(store);

        this.mockMvc = MockMvcBuilders.standaloneSetup(authController, commonDocsController)
                                      .setControllerAdvice(new GlobalControllerAdvice())
                                      .addInterceptors(authInterceptor)
                                      .setCustomArgumentResolvers(authAccountInfoArgumentResolver)
                                      .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                      .addFilters(new CharacterEncodingFilter("UTF-8", true))
                                      .alwaysDo(print())
                                      .alwaysDo(restDocs)
                                      .build();
    }
}
