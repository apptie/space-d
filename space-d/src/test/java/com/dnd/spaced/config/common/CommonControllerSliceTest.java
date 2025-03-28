package com.dnd.spaced.config.common;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.dnd.spaced.config.docs.RestDocsConfiguration;
import com.dnd.spaced.config.docs.snippet.DocsController;
import com.dnd.spaced.config.docs.snippet.exceptions.account.AccountExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.auth.AuthExceptionController;
import com.dnd.spaced.config.stub.StudAccountRepository;
import com.dnd.spaced.core.account.application.AccountService;
import com.dnd.spaced.core.account.presentation.AccountController;
import com.dnd.spaced.core.admin.application.AdminReportService;
import com.dnd.spaced.core.admin.application.AdminTodayQuizService;
import com.dnd.spaced.core.admin.application.AdminWordService;
import com.dnd.spaced.core.admin.presentation.AdminAuthenticationController;
import com.dnd.spaced.core.admin.presentation.AdminReportController;
import com.dnd.spaced.core.admin.presentation.AdminTodayQuizController;
import com.dnd.spaced.core.admin.presentation.AdminWordController;
import com.dnd.spaced.core.auth.application.BlacklistTokenService;
import com.dnd.spaced.core.auth.application.InitAccountCareerInfoService;
import com.dnd.spaced.core.auth.application.TokenService;
import com.dnd.spaced.core.auth.presentation.AuthController;
import com.dnd.spaced.core.bookmark.application.BookmarkService;
import com.dnd.spaced.core.bookmark.presentation.BookmarkController;
import com.dnd.spaced.core.comment.application.CommentService;
import com.dnd.spaced.core.comment.presentation.CommentController;
import com.dnd.spaced.core.image.application.LocalImageService;
import com.dnd.spaced.core.image.presentation.LocalImageController;
import com.dnd.spaced.core.like.application.LikeService;
import com.dnd.spaced.core.like.presentation.LikeController;
import com.dnd.spaced.core.quiz.application.QuizService;
import com.dnd.spaced.core.quiz.application.TodayQuizService;
import com.dnd.spaced.core.quiz.presentation.QuizController;
import com.dnd.spaced.core.quiz.presentation.TodayQuizController;
import com.dnd.spaced.core.report.application.ReportService;
import com.dnd.spaced.core.report.presentation.ReportController;
import com.dnd.spaced.core.skill.application.SkillService;
import com.dnd.spaced.core.skill.presentation.SkillController;
import com.dnd.spaced.core.word.application.WordService;
import com.dnd.spaced.core.word.presentation.WordController;
import com.dnd.spaced.global.auth.AuthStore;
import com.dnd.spaced.global.auth.interceptor.AuthInterceptor;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfoArgumentResolver;
import com.dnd.spaced.global.auth.resolver.GuestAccountInfoArgumentResolver;
import com.dnd.spaced.global.exception.GlobalControllerAdvice;
import com.dnd.spaced.global.log.QueryTraceInterceptor;
import com.dnd.spaced.global.resolver.admin.report.ReportPageableArgumentResolver;
import com.dnd.spaced.global.resolver.bookmark.BookmarkPageableArgumentResolver;
import com.dnd.spaced.global.resolver.comment.CommentPageableArgumentResolver;
import com.dnd.spaced.global.resolver.quiz.GradedAnswerPageableArgumentResolver;
import com.dnd.spaced.global.resolver.quiz.QuizPageableArgumentResolver;
import com.dnd.spaced.global.resolver.word.WordPageableArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
import org.springframework.http.MediaType;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = {
                DocsController.class, AccountExceptionController.class, AuthExceptionController.class,

                AuthController.class, AdminReportController.class, AccountController.class,
                WordController.class, CommentController.class, LikeController.class, QuizController.class,
                TodayQuizController.class, LocalImageController.class, ReportController.class,
                BookmarkController.class, SkillController.class, AdminAuthenticationController.class,
                AdminWordController.class, AdminTodayQuizController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthInterceptor.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = QueryTraceInterceptor.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthAccountInfoArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GuestAccountInfoArgumentResolver.class)
        }
)
@Import(RestDocsConfiguration.class)
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CommonControllerSliceTest {

    @Autowired
    DocsController commonDocsController;

    @Autowired
    AccountExceptionController accountExceptionController;

    @Autowired
    AuthExceptionController authExceptionController;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected RestDocumentationContextProvider provider;

    @Autowired
    AuthController authController;

    @Autowired
    AdminReportController adminReportController;

    @Autowired
    AccountController accountController;

    @Autowired
    WordController wordController;

    @Autowired
    CommentController commentController;

    @Autowired
    LikeController likeController;

    @Autowired
    QuizController quizController;

    @Autowired
    TodayQuizController todayQuizController;

    @Autowired
    LocalImageController localImageController;

    @Autowired
    ReportController reportController;

    @Autowired
    BookmarkController bookmarkController;

    @Autowired
    SkillController skillController;

    @Autowired
    AdminWordController adminWordController;

    @Autowired
    AdminAuthenticationController adminAuthenticationController;

    @Autowired
    AdminTodayQuizController adminTodayQuizController;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected BlacklistTokenService blacklistTokenService;

    @MockBean
    protected InitAccountCareerInfoService initAccountCareerInfoService;

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

    @MockBean
    protected AdminTodayQuizService adminTodayQuizService;

    @MockBean
    protected QuizService quizService;

    @MockBean
    protected TodayQuizService todayQuizService;

    @MockBean
    protected LocalImageService localImageService;

    @MockBean
    protected ReportService reportService;

    @MockBean
    protected AdminReportService adminReportService;

    @MockBean
    protected BookmarkService bookmarkService;

    @MockBean
    protected SkillService skillService;

    protected MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        AuthStore store = new AuthStore();
        AuthInterceptor authInterceptor = new AuthInterceptor(store);
        AuthAccountInfoArgumentResolver authAccountInfoArgumentResolver = new AuthAccountInfoArgumentResolver(store, new StudAccountRepository());
        GuestAccountInfoArgumentResolver guestAccountInfoArgumentResolver = new GuestAccountInfoArgumentResolver(store);
        WordPageableArgumentResolver wordPageableArgumentResolver = new WordPageableArgumentResolver();
        CommentPageableArgumentResolver commentPageableArgumentResolver = new CommentPageableArgumentResolver();
        GradedAnswerPageableArgumentResolver gradedAnswerPageableArgumentResolver = new GradedAnswerPageableArgumentResolver();
        BookmarkPageableArgumentResolver bookmarkPageableArgumentResolver = new BookmarkPageableArgumentResolver();
        ReportPageableArgumentResolver reportPageableArgumentResolver = new ReportPageableArgumentResolver();
        QuizPageableArgumentResolver quizPageableArgumentResolver = new QuizPageableArgumentResolver();
        MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ResourceHttpMessageConverter resourceMessageConverter = new ResourceHttpMessageConverter();
        resourceMessageConverter.setSupportedMediaTypes(
                List.of(
                        MediaType.IMAGE_PNG,
                        MediaType.IMAGE_JPEG,
                        MediaType.IMAGE_GIF
                )
        );


        this.mockMvc = MockMvcBuilders.standaloneSetup(
                                              authController,
                                              adminReportController,
                                              accountController,
                                              commonDocsController,
                                              wordController,
                                              commentController,
                                              likeController,
                                              quizController,
                                              todayQuizController,
                                              localImageController,
                                              reportController,
                                              bookmarkController,
                                              skillController,
                                              adminWordController,
                                              adminAuthenticationController,
                                              adminTodayQuizController,
                                              accountExceptionController,
                                              authExceptionController
                                      )
                                      .setControllerAdvice(new GlobalControllerAdvice())
                                      .setMessageConverters(jacksonMessageConverter, resourceMessageConverter)
                                      .addInterceptors(authInterceptor)
                                      .setCustomArgumentResolvers(
                                              authAccountInfoArgumentResolver,
                                              guestAccountInfoArgumentResolver,
                                              wordPageableArgumentResolver,
                                              commentPageableArgumentResolver,
                                              gradedAnswerPageableArgumentResolver,
                                              bookmarkPageableArgumentResolver,
                                              reportPageableArgumentResolver,
                                              quizPageableArgumentResolver
                                      )
                                      .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                      .addFilters(new CharacterEncodingFilter("UTF-8", true))
                                      .alwaysDo(print())
                                      .alwaysDo(restDocs)
                                      .build();
    }
}
