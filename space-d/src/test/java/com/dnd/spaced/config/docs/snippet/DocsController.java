package com.dnd.spaced.config.docs.snippet;

import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.enums.EnumDocs;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionDocs;
import com.dnd.spaced.core.account.domain.enums.Company;
import com.dnd.spaced.core.account.domain.enums.Experience;
import com.dnd.spaced.core.account.domain.enums.JobGroup;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizStatus;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.report.domain.enums.ReportReason;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.enums.PronunciationType;
import com.dnd.spaced.global.exception.code.AccountErrorCode;
import com.dnd.spaced.global.exception.code.AuthErrorCode;
import com.dnd.spaced.global.exception.code.BookmarkErrorCode;
import com.dnd.spaced.global.exception.code.CommentErrorCode;
import com.dnd.spaced.global.exception.code.ImageErrorCode;
import com.dnd.spaced.global.exception.code.LikeErrorCode;
import com.dnd.spaced.global.exception.code.QuizErrorCode;
import com.dnd.spaced.global.exception.code.ReportErrorCode;
import com.dnd.spaced.global.exception.code.SkillErrorCode;
import com.dnd.spaced.global.exception.code.WordErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import com.dnd.spaced.global.exception.translator.AccountExceptionTranslator;
import com.dnd.spaced.global.exception.translator.AuthExceptionTranslator;
import com.dnd.spaced.global.exception.translator.BookmarkExceptionTranslator;
import com.dnd.spaced.global.exception.translator.CommentExceptionTranslator;
import com.dnd.spaced.global.exception.translator.ExceptionTranslator;
import com.dnd.spaced.global.exception.translator.ImageExceptionTranslator;
import com.dnd.spaced.global.exception.translator.LikeExceptionTranslator;
import com.dnd.spaced.global.exception.translator.QuizExceptionTranslator;
import com.dnd.spaced.global.exception.translator.ReportExceptionTranslator;
import com.dnd.spaced.global.exception.translator.SkillExceptionTranslator;
import com.dnd.spaced.global.exception.translator.WordExceptionTranslator;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DocsController {

    @GetMapping("/enums")
    public ResponseEntity<CommonDocsResponse<EnumDocs>> findEnums() {
        Map<String, String> jobGroup = Arrays.stream(JobGroup.values())
                                             .collect(Collectors.toMap(Enum::name, JobGroup::getName));
        Map<String, String> company = Arrays.stream(Company.values())
                                            .collect(Collectors.toMap(Enum::name, Company::getName));
        Map<String, String> experience = Arrays.stream(Experience.values())
                                               .collect(Collectors.toMap(Enum::name, Experience::getName));
        Map<String, String> profileImageName = Arrays.stream(ProfileImageName.values())
                                                     .collect(
                                                             Collectors.toMap(Enum::name, ProfileImageName::getKorean)
                                                     );
        Map<String, String> category = Arrays.stream(Category.values())
                                             .collect(Collectors.toMap(Enum::name, Category::getName));
        Map<String, String> pronunciationType = Arrays.stream(PronunciationType.values())
                                                      .collect(
                                                              Collectors.toMap(Enum::name, PronunciationType::getName)
                                                      );
        Map<String, String> quizCategory = Arrays.stream(QuizCategory.values())
                                                 .collect(Collectors.toMap(Enum::name, QuizCategory::getName));
        Map<String, String> reportReason = Arrays.stream(ReportReason.values())
                                                 .collect(Collectors.toMap(Enum::name, ReportReason::getCause));
        Map<String, String> reportStatus = Arrays.stream(ReportStatus.values())
                                                 .collect(Collectors.toMap(Enum::name, ReportStatus::getName));
        Map<String, String> todayQuizStatus = Arrays.stream(TodayQuizStatus.values())
                                                    .collect(Collectors.toMap(Enum::name, TodayQuizStatus::getName));

        EnumDocs enumDocs = EnumDocs.builder()
                                    .jobGroup(jobGroup)
                                    .company(company)
                                    .experience(experience)
                                    .profileImageName(profileImageName)
                                    .category(category)
                                    .pronunciationType(pronunciationType)
                                    .quizCategory(quizCategory)
                                    .reportReason(reportReason)
                                    .reportStatus(reportStatus)
                                    .todayQuizStatus(todayQuizStatus)
                                    .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(enumDocs));
    }

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<ExceptionDocs>> findExceptions() {
        ExceptionDocs exceptionDocs = ExceptionDocs.builder()
                                                   .authProfileException(calculateAuthProfileException())
                                                   .refreshTokenException(calculateRefreshTokenException())
                                                   .registerBlacklistTokenException(
                                                           calculateRegisterBlacklistTokenException())
                                                   .withdrawalException(calculateWithdrawalException())
                                                   .changeCareerInfoException(calculateChangeCareerInfoException())
                                                   .changeProfileInfoException(calculateChangeProfileInfoException())
                                                   .findAccountInfoException(calculateFindAccountInfoException())
                                                   .saveWordException(calculateSaveWordException())
                                                   .updateWordExampleException(calculateUpdateWordExampleException())
                                                   .deleteWordExampleException(calculateDeleteWordExampleException())
                                                   .deletePronunciationException(
                                                           calculateDeletePronunciationException())
                                                   .readWordException(calculateReadWordException())
                                                   .saveCommentException(calculateSaveCommentException())
                                                   .deleteCommentException(calculateDeleteCommentException())
                                                   .updateCommentException(calculateUpdateCommentException())
                                                   .processLikeException(calculateProcessLikeException())
                                                   .createQuizException(calculateCreateQuizException())
                                                   .gradeQuizException(calculateGradeQuizException())
                                                   .findGradedAnswersAllByException(
                                                           calculateFindGradedAnswersAllByException())
                                                   .findGradedAnswersAllByQuizException(
                                                           calculateFindGradedAnswersAllByQuizException())
                                                   .findQuizByException(calculateFindQuizByException())
                                                   .findLatestTodayQuizException(
                                                           calculateFindLatestTodayQuizException())
                                                   .findTodayQuizByException(calculateFindTodayQuizByException())
                                                   .gradeTodayQuizException(calculateGradeTodayQuizException())
                                                   .findTodayQuizGradedAnswerByException(
                                                           calculateFindTodayQuizGradedAnswerByException())
                                                   .findTodayQuizGradedAnswersAllByException(
                                                           calculateFindTodayQuizGradedAnswersAllByException())
                                                   .createTodayQuizException(calculateCreateTodayQuizException())
                                                   .readLocalImageException(calculateLocalImageNotFoundException())
                                                   .reportException(calculateReportException())
                                                   .processReportException(calculateProcessReportException())
                                                   .createBookmarkException(calculateCreateBookmarkException())
                                                   .deleteBookmarkException(calculateDeleteBookmarkException())
                                                   .findAllBookmarkException(calculateFindAllBookmarkException())
                                                   .findSkillException(calculateFindSkillException())
                                                   .readQuizzesException(calculateReadQuizzesException())
                                                   .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(exceptionDocs));
    }

    private Map<String, ExceptionContent> calculateReadQuizzesException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }


    private Map<String, ExceptionContent> calculateFindSkillException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processSkillException(
                exceptionContent,
                SkillErrorCode.QUIZ_METADATA_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateFindAllBookmarkException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateDeleteBookmarkException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processBookmarkException(
                exceptionContent,
                BookmarkErrorCode.BOOKMARK_NOT_FOUND_EXCEPTION,
                BookmarkErrorCode.FORBIDDEN_DELETE_BOOKMARK_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateCreateBookmarkException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processBookmarkException(
                exceptionContent,
                BookmarkErrorCode.WORD_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateProcessReportException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putForbiddenExceptionContent(exceptionContent);
        processReportException(
                exceptionContent,
                ReportErrorCode.REPORT_NOT_FOUND_EXCEPTION,
                ReportErrorCode.REPORT_STATUS_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateReportException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putMethodArgumentNotValidExceptionContent(exceptionContent, "commentId", "cause");
        processReportException(
                exceptionContent,
                ReportErrorCode.CANNOT_REPORT_OWN_COMMENT_EXCEPTION,
                ReportErrorCode.REPORT_REASON_NOT_FOUND_EXCEPTION,
                ReportErrorCode.COMMENT_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateLocalImageNotFoundException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        processImageException(exceptionContent, ImageErrorCode.IMAGE_FILE_NOT_FOUND_EXCEPTION);

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateCreateTodayQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putForbiddenExceptionContent(exceptionContent);
        processQuizException(
                exceptionContent,
                QuizErrorCode.WORD_METADATA_NOT_FOUND_EXCEPTION,
                QuizErrorCode.INVALID_TODAY_QUIZ_WORD_COUNT_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateFindLatestTodayQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        processQuizException(
                exceptionContent,
                QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateFindTodayQuizByException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        processQuizException(
                exceptionContent,
                QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateGradeTodayQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putMethodArgumentNotValidExceptionContent(exceptionContent, "answer");
        processQuizException(
                exceptionContent,
                QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION,
                QuizErrorCode.INVALID_SUBMITTED_TODAY_QUIZ_OPTION_INDEX_EXCEPTION,
                QuizErrorCode.ALREADY_GRADE_TODAY_QUIZ_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateFindTodayQuizGradedAnswerByException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        processQuizException(
                exceptionContent,
                QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateFindTodayQuizGradedAnswersAllByException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateCreateQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processQuizException(
                exceptionContent,
                QuizErrorCode.INVALID_QUIZ_CATEGORY_NAME_EXCEPTION,
                QuizErrorCode.WORD_METADATA_NOT_FOUND_EXCEPTION,
                QuizErrorCode.INVALID_QUIZ_WORD_COUNT_EXCEPTION,
                QuizErrorCode.WORD_METADATA_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateGradeQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putMethodArgumentNotValidExceptionContent(exceptionContent, "answers");
        processQuizException(
                exceptionContent,
                QuizErrorCode.QUIZ_NOT_FOUND_EXCEPTION,
                QuizErrorCode.ALREADY_GRADE_QUIZ_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateFindGradedAnswersAllByException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateFindGradedAnswersAllByQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateFindQuizByException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processQuizException(
                exceptionContent,
                QuizErrorCode.QUIZ_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateProcessLikeException() {
        Map<String, ExceptionContent> processLikeException = new LinkedHashMap<>();

        processLikeException(
                processLikeException,
                LikeErrorCode.FORBIDDEN_LIKE,
                LikeErrorCode.ASSOCIATION_COMMENT_NOT_FOUND
        );

        return processLikeException;
    }

    private Map<String, ExceptionContent> calculateUpdateCommentException() {
        Map<String, ExceptionContent> updateCommentException = new LinkedHashMap<>();

        processCommentException(
                updateCommentException,
                CommentErrorCode.ASSOCIATION_ACCOUNT_NOT_FOUND,
                CommentErrorCode.ASSOCIATION_WORD_NOT_FOUND,
                CommentErrorCode.FORBIDDEN_COMMENT,
                CommentErrorCode.INVALID_COMMENT_CONTENT
        );

        return updateCommentException;
    }

    private Map<String, ExceptionContent> calculateDeleteCommentException() {
        Map<String, ExceptionContent> deleteCommentException = new LinkedHashMap<>();

        processCommentException(
                deleteCommentException,
                CommentErrorCode.ASSOCIATION_ACCOUNT_NOT_FOUND,
                CommentErrorCode.ASSOCIATION_WORD_NOT_FOUND,
                CommentErrorCode.FORBIDDEN_COMMENT
        );

        return deleteCommentException;
    }

    private Map<String, ExceptionContent> calculateSaveCommentException() {
        Map<String, ExceptionContent> saveCommentException = new LinkedHashMap<>();

        processCommentException(
                saveCommentException,
                CommentErrorCode.ASSOCIATION_ACCOUNT_NOT_FOUND,
                CommentErrorCode.ASSOCIATION_WORD_NOT_FOUND,
                CommentErrorCode.INVALID_COMMENT_CONTENT
        );

        return saveCommentException;
    }

    private Map<String, ExceptionContent> calculateReadWordException() {
        Map<String, ExceptionContent> readWordException = new LinkedHashMap<>();

        processWordException(readWordException, WordErrorCode.WORD_NOT_FOUND);

        return readWordException;
    }

    private Map<String, ExceptionContent> calculateDeletePronunciationException() {
        Map<String, ExceptionContent> deletePronunciationException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(deletePronunciationException);
        putForbiddenExceptionContent(deletePronunciationException);
        processWordException(deletePronunciationException, WordErrorCode.PRONUNCIATION_DELETION_NOT_ALLOWED);

        return deletePronunciationException;
    }


    private Map<String, ExceptionContent> calculateDeleteWordExampleException() {
        Map<String, ExceptionContent> deleteWordExampleException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(deleteWordExampleException);
        putForbiddenExceptionContent(deleteWordExampleException);
        processWordException(deleteWordExampleException, WordErrorCode.WORD_EXAMPLE_DELETION_NOT_ALLOWED);

        return deleteWordExampleException;
    }

    private Map<String, ExceptionContent> calculateUpdateWordExampleException() {
        Map<String, ExceptionContent> updateWordExampleException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(updateWordExampleException);
        putForbiddenExceptionContent(updateWordExampleException);
        putMethodArgumentNotValidExceptionContent(updateWordExampleException, "example");
        processWordException(
                updateWordExampleException,
                WordErrorCode.INVALID_WORD_EXAMPLE_CONTENT,
                WordErrorCode.UNEXPECTED_UPDATE_WORD_EXAMPLE_COUNT
        );

        return updateWordExampleException;
    }

    private Map<String, ExceptionContent> calculateSaveWordException() {
        Map<String, ExceptionContent> saveWordException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(saveWordException);
        putForbiddenExceptionContent(saveWordException);
        putMethodArgumentNotValidExceptionContent(
                saveWordException,
                "name",
                "meaning",
                "categoryName",
                "pronunciations",
                "pronunciation",
                "typeName",
                "examples"
        );
        processWordException(saveWordException, WordErrorCode.values());

        return saveWordException;
    }

    private Map<String, ExceptionContent> calculateFindAccountInfoException() {
        Map<String, ExceptionContent> findAccountInfoException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(findAccountInfoException);

        return findAccountInfoException;
    }

    private Map<String, ExceptionContent> calculateChangeProfileInfoException() {
        Map<String, ExceptionContent> changeProfileInfoException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(changeProfileInfoException);
        putMethodArgumentNotValidExceptionContent(
                changeProfileInfoException,
                "nickname",
                "profileImageKoreanName"
        );
        processAccountException(
                changeProfileInfoException,
                AccountErrorCode.INVALID_NICKNAME,
                AccountErrorCode.INVALID_PROFILE_NAME,
                AccountErrorCode.INVALID_PROFILE_IMAGE
        );

        return changeProfileInfoException;
    }

    private Map<String, ExceptionContent> calculateChangeCareerInfoException() {
        Map<String, ExceptionContent> changeCareerInfoException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(changeCareerInfoException);
        putMethodArgumentNotValidExceptionContent(
                changeCareerInfoException,
                "jobGroupName",
                "companyName",
                "experienceName"
        );
        processAccountException(
                changeCareerInfoException,
                AccountErrorCode.INVALID_COMPANY,
                AccountErrorCode.INVALID_EXPERIENCE,
                AccountErrorCode.INVALID_JOB_GROUP
        );

        return changeCareerInfoException;
    }

    private Map<String, ExceptionContent> calculateWithdrawalException() {
        Map<String, ExceptionContent> withdrawalException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(withdrawalException);
        processAccountException(withdrawalException, AccountErrorCode.FORBIDDEN_ACCOUNT);

        return withdrawalException;
    }

    private Map<String, ExceptionContent> calculateRegisterBlacklistTokenException() {
        Map<String, ExceptionContent> registerBlacklistTokenException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(registerBlacklistTokenException);
        putForbiddenExceptionContent(registerBlacklistTokenException);
        putMethodArgumentNotValidExceptionContent(registerBlacklistTokenException, "accountId");
        processAuthException(registerBlacklistTokenException, AuthErrorCode.INVALID_BLACKLIST_TOKEN_CONTENT_EXCEPTION);

        return registerBlacklistTokenException;
    }

    private Map<String, ExceptionContent> calculateRefreshTokenException() {
        Map<String, ExceptionContent> refreshTokenException = new LinkedHashMap<>();

        processAuthException(
                refreshTokenException,
                AuthErrorCode.REFRESH_TOKEN_NOT_FOUND_EXCEPTION,
                AuthErrorCode.EXPIRED_TOKEN_EXCEPTION,
                AuthErrorCode.BLOCKED_TOKEN_EXCEPTION,
                AuthErrorCode.ROTATION_REFRESH_TOKEN_MISMATCH_EXCEPTION
        );

        return refreshTokenException;
    }

    private Map<String, ExceptionContent> calculateAuthProfileException() {
        Map<String, ExceptionContent> authProfileException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(authProfileException);
        putForbiddenExceptionContent(authProfileException);
        putMethodArgumentNotValidExceptionContent(
                authProfileException,
                "jobGroupName",
                "companyName",
                "experienceName"
        );

        processAuthException(authProfileException, AuthErrorCode.FORBIDDEN_INIT_CAREER_INFO_EXCEPTION);
        processAccountException(
                authProfileException,
                AccountErrorCode.INVALID_COMPANY,
                AccountErrorCode.INVALID_EXPERIENCE,
                AccountErrorCode.INVALID_JOB_GROUP
        );

        return authProfileException;
    }

    private void putUnauthorizedExceptionContent(Map<String, ExceptionContent> target) {
        target.put(
                "UNAUTHORIZED",
                new ExceptionContent(HttpStatus.UNAUTHORIZED, "로그인이 필요한 기능입니다.")
        );
    }

    private void putForbiddenExceptionContent(Map<String, ExceptionContent> target) {
        target.put("FORBIDDEN", new ExceptionContent(HttpStatus.FORBIDDEN, "권한이 없습니다."));
    }

    private void putMethodArgumentNotValidExceptionContent(Map<String, ExceptionContent> target, String... inputs) {
        target.put("INVALID_DATA", createMethodArgumentNotValidExceptionDto(inputs));
    }

    private void processSkillException(Map<String, ExceptionContent> target, SkillErrorCode... errorCodes) {
        for (SkillErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = SkillExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processBookmarkException(Map<String, ExceptionContent> target, BookmarkErrorCode... errorCodes) {
        for (BookmarkErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = BookmarkExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processReportException(Map<String, ExceptionContent> target, ReportErrorCode... errorCodes) {
        for (ReportErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = ReportExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processImageException(Map<String, ExceptionContent> target, ImageErrorCode... errorCodes) {
        for (ImageErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = ImageExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processQuizException(Map<String, ExceptionContent> target, QuizErrorCode... errorCodes) {
        for (QuizErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = QuizExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processLikeException(Map<String, ExceptionContent> target, LikeErrorCode... errorCodes) {
        for (LikeErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = LikeExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processCommentException(Map<String, ExceptionContent> target, CommentErrorCode... errorCodes) {
        for (CommentErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = CommentExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processWordException(Map<String, ExceptionContent> target, WordErrorCode... errorCodes) {
        for (WordErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = WordExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processAuthException(Map<String, ExceptionContent> target, AuthErrorCode... errorCodes) {
        for (AuthErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = AuthExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processAccountException(Map<String, ExceptionContent> target, AccountErrorCode... errorCodes) {
        for (AccountErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = AccountExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    private void processExceptionContent(Map<String, ExceptionContent> target, ExceptionTranslator translator) {
        ExceptionDto exceptionDto = translator.translate();
        HttpStatus httpStatus = translator.getHttpStatus();
        ExceptionContent exceptionContent = new ExceptionContent(
                httpStatus,
                exceptionDto.message()
        );

        target.put(exceptionDto.code(), exceptionContent);
    }

    private ExceptionContent createMethodArgumentNotValidExceptionDto(String... inputs) {
        return new ExceptionContent(
                HttpStatus.BAD_REQUEST,
                "유효한 입력 값이 아닙니다. (" + String.join(", ", inputs) + ")"
        );
    }
}
