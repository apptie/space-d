package com.dnd.spaced.config.docs.snippet.exceptions.account;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.AccountErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/accounts")
public class AccountExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<AccountExceptionDocs>> findExceptions() {
        AccountExceptionDocs accountExceptionDocs =
                AccountExceptionDocs.builder()
                                    .authProfileException(calculateAuthProfileException())
                                    .withdrawalException(calculateWithdrawalException())
                                    .changeCareerInfoException(calculateChangeCareerInfoException())
                                    .changeProfileInfoException(calculateChangeProfileInfoException())
                                    .findAccountInfoException(calculateFindAccountInfoException())
                                    .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(accountExceptionDocs));
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

        processAccountException(
                authProfileException,
                AccountErrorCode.INVALID_COMPANY,
                AccountErrorCode.INVALID_EXPERIENCE,
                AccountErrorCode.INVALID_JOB_GROUP,
                AccountErrorCode.FORBIDDEN_INIT_CAREER_INFO_EXCEPTION
        );

        return authProfileException;
    }
}
