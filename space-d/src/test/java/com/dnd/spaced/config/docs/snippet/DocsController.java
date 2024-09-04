package com.dnd.spaced.config.docs.snippet;

import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.enums.EnumDocs;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionDocs;
import com.dnd.spaced.core.account.domain.Company;
import com.dnd.spaced.core.account.domain.Experience;
import com.dnd.spaced.core.account.domain.JobGroup;
import com.dnd.spaced.global.exception.code.AccountErrorCode;
import com.dnd.spaced.global.exception.code.AuthErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import com.dnd.spaced.global.exception.translator.AccountExceptionTranslator;
import com.dnd.spaced.global.exception.translator.AuthExceptionTranslator;
import com.dnd.spaced.global.exception.translator.ExceptionTranslator;
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

        EnumDocs enumDocs = EnumDocs.builder()
                                    .jobGroup(jobGroup)
                                    .company(company)
                                    .experience(experience)
                                    .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(enumDocs));
    }

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<ExceptionDocs>> findExceptions() {
        Map<String, ExceptionContent> authProfileException = calculateAuthProfileException();
        Map<String, ExceptionContent> refreshTokenException = calculateRefreshTokenException();
        ExceptionDocs exceptionDocs = ExceptionDocs.builder()
                                                   .authProfileException(authProfileException)
                                                   .refreshTokenException(refreshTokenException)
                                                   .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(exceptionDocs));
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

        ExceptionContent unauthorizedExceptionContent = new ExceptionContent(
                HttpStatus.UNAUTHORIZED,
                "로그인이 필요한 기능입니다."
        );

        authProfileException.put("UNAUTHORIZED", unauthorizedExceptionContent);

        processAuthException(authProfileException, AuthErrorCode.FORBIDDEN_INIT_CAREER_INFO);
        processAccountException(authProfileException, AccountErrorCode.INVALID_COMPANY,
                AccountErrorCode.INVALID_EXPERIENCE, AccountErrorCode.INVALID_JOB_GROUP);

        return authProfileException;
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
}
