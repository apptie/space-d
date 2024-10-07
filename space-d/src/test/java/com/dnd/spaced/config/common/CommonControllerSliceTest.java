package com.dnd.spaced.config.common;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.dnd.spaced.config.docs.snippet.DocsController;
import com.dnd.spaced.config.docs.RestDocsConfiguration;
import com.dnd.spaced.core.account.application.AccountService;
import com.dnd.spaced.core.account.presentation.AccountController;
import com.dnd.spaced.core.admin.application.AdminWordService;
import com.dnd.spaced.core.admin.presentation.AdminController;
import com.dnd.spaced.core.auth.application.TokenService;
import com.dnd.spaced.core.auth.application.BlacklistTokenService;
import com.dnd.spaced.core.auth.application.InitAccountInfoService;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.core.auth.presentation.AuthController;
import com.dnd.spaced.core.comment.application.CommentService;
import com.dnd.spaced.core.comment.presentation.CommentController;
import com.dnd.spaced.core.like.application.LikeService;
import com.dnd.spaced.core.like.presentation.LikeController;
import com.dnd.spaced.core.word.application.WordService;
import com.dnd.spaced.core.word.presentation.WordController;
import com.dnd.spaced.global.auth.AuthStore;
import com.dnd.spaced.global.auth.interceptor.AuthInterceptor;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfoArgumentResolver;
import com.dnd.spaced.global.exception.GlobalControllerAdvice;
import com.dnd.spaced.global.resolver.comment.CommonCommentPageableArgumentResolver;
import com.dnd.spaced.global.resolver.word.CommonWordPageableArgumentResolver;
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
                AuthController.class, DocsController.class, AdminController.class, AccountController.class,
                WordController.class, CommentController.class, LikeController.class
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
    protected DocsController commonDocsController;

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

    @Autowired
    protected AdminController adminController;

    @Autowired
    protected AccountController accountController;

    @Autowired
    protected WordController wordController;

    @Autowired
    protected CommentController commentController;

    @Autowired
    protected LikeController likeController;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected BlacklistTokenService blacklistTokenService;

    @MockBean
    protected InitAccountInfoService initAccountInfoService;

    @MockBean
    protected AdminWordService adminWordService;

    @MockBean
    protected TokenService tokenService;

    @MockBean
    protected WordService wordService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected LikeService likeService;

    protected MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        AuthStore store = new AuthStore();
        AuthInterceptor authInterceptor = new AuthInterceptor(store);
        AuthAccountInfoArgumentResolver authAccountInfoArgumentResolver = new AuthAccountInfoArgumentResolver(store);
        CommonWordPageableArgumentResolver commonWordPageableArgumentResolver = new CommonWordPageableArgumentResolver();
        CommonCommentPageableArgumentResolver commonCommentPageableArgumentResolver = new CommonCommentPageableArgumentResolver();

        this.mockMvc = MockMvcBuilders.standaloneSetup(
                                              authController,
                                              adminController,
                                              accountController,
                                              commonDocsController,
                                              wordController,
                                              commentController,
                                              likeController
                                      )
                                      .setControllerAdvice(new GlobalControllerAdvice())
                                      .addInterceptors(authInterceptor)
                                      .setCustomArgumentResolvers(
                                              authAccountInfoArgumentResolver,
                                              commonWordPageableArgumentResolver,
                                              commonCommentPageableArgumentResolver
                                      )
                                      .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                      .addFilters(new CharacterEncodingFilter("UTF-8", true))
                                      .alwaysDo(print())
                                      .alwaysDo(restDocs)
                                      .build();
    }
}
