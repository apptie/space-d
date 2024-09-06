package com.dnd.spaced.config.docs.snippet;

import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.enums.EnumDocs;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionDocs;
import com.dnd.spaced.core.account.domain.Company;
import com.dnd.spaced.core.account.domain.Experience;
import com.dnd.spaced.core.account.domain.JobGroup;
import com.dnd.spaced.core.account.domain.ProfileImageName;
import com.dnd.spaced.core.word.domain.Category;
import com.dnd.spaced.core.word.domain.PronunciationType;
import com.dnd.spaced.global.exception.code.AccountErrorCode;
import com.dnd.spaced.global.exception.code.AuthErrorCode;
import com.dnd.spaced.global.exception.code.WordErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import com.dnd.spaced.global.exception.translator.AccountExceptionTranslator;
import com.dnd.spaced.global.exception.translator.AuthExceptionTranslator;
import com.dnd.spaced.global.exception.translator.ExceptionTranslator;
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
                                             .collect(
                                                     Collectors.toMap(Enum::name, Category::getName)
                                             );
        Map<String, String> pronunciationType = Arrays.stream(PronunciationType.values())
                                                      .collect(
                                                              Collectors.toMap(Enum::name, PronunciationType::getName)
                                                      );

        EnumDocs enumDocs = EnumDocs.builder()
                                    .jobGroup(jobGroup)
                                    .company(company)
                                    .experience(experience)
                                    .profileImageName(profileImageName)
                                    .category(category)
                                    .pronunciationType(pronunciationType)
                                    .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(enumDocs));
    }

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<ExceptionDocs>> findExceptions() {
        ExceptionDocs exceptionDocs = ExceptionDocs.builder()
                                                   .authProfileException(calculateAuthProfileException())
                                                   .refreshTokenException(calculateRefreshTokenException())
                                                   .registerBlacklistTokenException(
                                                           calculateRegisterBlacklistTokenException()
                                                   )
                                                   .withdrawalException(calculateWithdrawalException())
                                                   .changeCareerInfoException(calculateChangeCareerInfoException())
                                                   .changeProfileInfoException(calculateChangeProfileInfoException())
                                                   .findAccountInfoException(calculateFindAccountInfoException())
                                                   .saveWordException(calculateSaveWordException())
                                                   .updateWordExampleException(calculateUpdateWordExampleException())
                                                   .deleteWordExampleException(calculateDeleteWordExampleException())
                                                   .deletePronunciationException(calculateDeletePronunciationException())
                                                   .readWordException(calculateReadWordException())
                                                   .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(exceptionDocs));
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

        return deletePronunciationException;
    }


    private Map<String, ExceptionContent> calculateDeleteWordExampleException() {
        Map<String, ExceptionContent> deleteWordExampleException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(deleteWordExampleException);
        putForbiddenExceptionContent(deleteWordExampleException);

        return deleteWordExampleException;
    }

    private Map<String, ExceptionContent> calculateUpdateWordExampleException() {
        Map<String, ExceptionContent> updateWordExampleException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(updateWordExampleException);
        putForbiddenExceptionContent(updateWordExampleException);
        putMethodArgumentNotValidExceptionContent(updateWordExampleException, "example");
        processWordException(updateWordExampleException, WordErrorCode.INVALID_WORD_EXAMPLE_CONTENT);

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
                "originNickname",
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
        processAuthException(registerBlacklistTokenException, AuthErrorCode.INVALID_BLACKLIST_TOKEN_CONTENT);

        return registerBlacklistTokenException;
    }

    private Map<String, ExceptionContent> calculateRefreshTokenException() {
        Map<String, ExceptionContent> refreshTokenException = new LinkedHashMap<>();

        processAuthException(
                refreshTokenException,
                AuthErrorCode.REFRESH_TOKEN_NOT_FOUND,
                AuthErrorCode.EXPIRED_TOKEN,
                AuthErrorCode.BLOCKED_TOKEN,
                AuthErrorCode.ROTATION_REFRESH_TOKEN_MISMATCH
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

        processAuthException(authProfileException, AuthErrorCode.FORBIDDEN_INIT_CAREER_INFO);
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
