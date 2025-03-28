package com.dnd.spaced.config.docs.snippet;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
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
import java.util.Map;
import org.springframework.http.HttpStatus;

public abstract class CommonExceptionController {

    protected void processExceptionContent(Map<String, ExceptionContent> target, ExceptionTranslator translator) {
        ExceptionDto exceptionDto = translator.translate();
        HttpStatus httpStatus = translator.getHttpStatus();
        ExceptionContent exceptionContent = new ExceptionContent(
                httpStatus,
                exceptionDto.message()
        );

        target.put(exceptionDto.code(), exceptionContent);
    }

    protected void putUnauthorizedExceptionContent(Map<String, ExceptionContent> target) {
        target.put(
                "UNAUTHORIZED",
                new ExceptionContent(HttpStatus.UNAUTHORIZED, "로그인이 필요한 기능입니다.")
        );
    }

    protected void putForbiddenExceptionContent(Map<String, ExceptionContent> target) {
        target.put("FORBIDDEN", new ExceptionContent(HttpStatus.FORBIDDEN, "권한이 없습니다."));
    }

    protected void putMethodArgumentNotValidExceptionContent(Map<String, ExceptionContent> target, String... inputs) {
        target.put("INVALID_DATA", createMethodArgumentNotValidExceptionContent(inputs));
    }

    protected ExceptionContent createMethodArgumentNotValidExceptionContent(String... inputs) {
        return new ExceptionContent(
                HttpStatus.BAD_REQUEST,
                "유효한 입력 값이 아닙니다. (" + String.join(", ", inputs) + ")"
        );
    }

    protected void processSkillException(Map<String, ExceptionContent> target, SkillErrorCode... errorCodes) {
        for (SkillErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = SkillExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processBookmarkException(Map<String, ExceptionContent> target, BookmarkErrorCode... errorCodes) {
        for (BookmarkErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = BookmarkExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processReportException(Map<String, ExceptionContent> target, ReportErrorCode... errorCodes) {
        for (ReportErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = ReportExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processImageException(Map<String, ExceptionContent> target, ImageErrorCode... errorCodes) {
        for (ImageErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = ImageExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processQuizException(Map<String, ExceptionContent> target, QuizErrorCode... errorCodes) {
        for (QuizErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = QuizExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processLikeException(Map<String, ExceptionContent> target, LikeErrorCode... errorCodes) {
        for (LikeErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = LikeExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processCommentException(Map<String, ExceptionContent> target, CommentErrorCode... errorCodes) {
        for (CommentErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = CommentExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processWordException(Map<String, ExceptionContent> target, WordErrorCode... errorCodes) {
        for (WordErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = WordExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processAuthException(Map<String, ExceptionContent> target, AuthErrorCode... errorCodes) {
        for (AuthErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = AuthExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }

    protected void processAccountException(Map<String, ExceptionContent> target, AccountErrorCode... errorCodes) {
        for (AccountErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = AccountExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }
}
